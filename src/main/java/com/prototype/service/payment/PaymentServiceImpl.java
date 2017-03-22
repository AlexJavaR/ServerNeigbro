package com.prototype.service.payment;

import com.prototype.model.*;
import com.prototype.model.event.Event;
import com.prototype.model.event.payment.*;
import com.prototype.repository.address.AddressRepository;
import com.prototype.repository.event.EventRepository;
import com.prototype.repository.user.UserRepository;
import com.prototype.service.email.EmailService;
import com.prototype.to.ApartmentsWithDebt;
import com.prototype.to.SingleManagerPayment;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service("PaymentService")
public class PaymentServiceImpl implements PaymentService {

    private final EventRepository eventRepository;

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    private final EmailService emailService;

    @Autowired
    public PaymentServiceImpl(EventRepository eventRepository, AddressRepository addressRepository,
                              UserRepository userRepository, EmailService emailService) {
        this.eventRepository = eventRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public List<BillEvent> findAllBillsOfApartment(BigInteger userId, BigInteger addressId, String apartment) {
        ObjectId objectAddressId = convertBigIntegerToObjectId(addressId);
        if (apartment == null || objectAddressId == null) {
            return null;
        }
        List<BillEvent> allBillsOfApartment = eventRepository.findAllBillsOfApartment(objectAddressId, apartment);
        allBillsOfApartment.sort((abc1, abc2) -> Boolean.compare(abc1.isSettled(), abc2.isSettled()));
        return allBillsOfApartment;
    }

    @Override
    public List<BillEvent> findAllBillsOfApartmentByManager(BigInteger managerId, BigInteger addressId, String apartment) {
        ObjectId objectAddressId = convertBigIntegerToObjectId(addressId);
        if (objectAddressId == null) {
            return null;
        }
        User managerUser = userRepository.findOne(managerId);
        if (userRepository.getRoleByAddress(managerUser, addressRepository.findOne(addressId)) == Role.MANAGER) {
            List<BillEvent> allBillsOfApartment = eventRepository.findAllBillsOfApartment(objectAddressId, apartment);
            allBillsOfApartment.sort((abc1, abc2) -> Boolean.compare(abc1.isSettled(), abc2.isSettled()));
            return allBillsOfApartment;
        }
        return null;
    }


    @Override
    public List<ManagerPaymentEvent> findAllManagerPaymentEvent(BigInteger managerId, BigInteger addressId) {
        ObjectId objectAddressId = convertBigIntegerToObjectId(addressId);
        if (objectAddressId == null) {
            return null;
        }
        User managerUser = userRepository.findOne(managerId);
        if (userRepository.getRoleByAddress(managerUser, addressRepository.findOne(addressId)) == Role.MANAGER) {
            return eventRepository.findAllManagerPaymentEvent(objectAddressId);
        }
        return null;
    }

    @Override
    public Integer getAmountDebtOfApartment(BigInteger userId, BigInteger addressId, String apartment) {
        ObjectId objectAddressId = convertBigIntegerToObjectId(addressId);
        if (apartment == null || objectAddressId == null) {
            return null;
        }
        List<BillEvent> unsettledBills = eventRepository.getAmountDebtOfApartment(objectAddressId, apartment);
        Integer amountDebt = 0;
        for (BillEvent bill : unsettledBills) {
            amountDebt += bill.getBalanceBill();
        }
        return amountDebt;
    }

    @Override
    public Integer getAmountDebtOfApartmentByManager(BigInteger managerId, BigInteger addressId, String apartment) {
        ObjectId objectAddressId = convertBigIntegerToObjectId(addressId);
        if (objectAddressId == null) {
            return null;
        }
        User managerUser = userRepository.findOne(managerId);
        if (userRepository.getRoleByAddress(managerUser, addressRepository.findOne(addressId)) == Role.MANAGER) {
            List<BillEvent> unsettledBills = eventRepository.getAmountDebtOfApartment(objectAddressId, apartment);
            Integer amountDebt = 0;
            for (BillEvent bill : unsettledBills) {
                amountDebt += bill.getBalanceBill();
            }
            return amountDebt;
        }
        return null;
    }

    @Override
    public List<ApartmentsWithDebt> getDebtOfAllApartmentsByManager(BigInteger managerId, BigInteger addressId) {
        List<ApartmentsWithDebt> apartmentsWithDebt = new ArrayList<>();
        User managerUser = userRepository.findOne(managerId);
        if (managerUser == null) {
            return null;
        }
        if (userRepository.getRoleByAddress(managerUser, addressRepository.findOne(addressId)) == Role.MANAGER) {
            List<String> listApartments = addressRepository.findOne(addressId).getListOfApartment();
            for (String apartment : listApartments) {
                Integer amountDebt = getAmountDebtOfApartmentByManager(managerId, addressId, apartment);
                apartmentsWithDebt.add(new ApartmentsWithDebt(apartment, amountDebt));
            }
            return apartmentsWithDebt;
        }
        return null;
    }

    @Override
    public BillEvent setStatusBillInProcess(BigInteger userId, BigInteger billId, Integer blockedAmount) {
        User currentUser = userRepository.findOne(userId);
        BillEvent billEvent = (BillEvent) eventRepository.findOne(billId);
        if (currentUser == null || billEvent == null) {
            return null;
        }
        if (userRepository.getAddressData(currentUser, billEvent.getAddress(), billEvent.getApartment()) != null) {
            if (!billEvent.isProcessed()) {
                billEvent.setProcessed(true);
                billEvent.setBlockedAmount(blockedAmount);
            }
            ActionListener taskPerformer = e -> {
                if (billEvent.isProcessed()) {
                    billEvent.setProcessed(false);
                    billEvent.setBlockedAmount(0);
                    eventRepository.save(billEvent);
                }
            };
            Timer timer = new Timer(1800000, taskPerformer);
            timer.start();
            timer.setRepeats(false);
            return eventRepository.save(billEvent);
        } else {
            return null;
        }
    }

    @Override
    public BillEvent setStatusBillWithoutInProcess(BigInteger userId, BigInteger billId, Integer blockedAmount) {
        User currentUser = userRepository.findOne(userId);
        BillEvent billEvent = (BillEvent) eventRepository.findOne(billId);
        if (currentUser == null || billEvent == null) {
            return null;
        }
        if (userRepository.getAddressData(currentUser, billEvent.getAddress(), billEvent.getApartment()) != null && billEvent.isProcessed()) {
            billEvent.setProcessed(false);
            billEvent.setBlockedAmount(0);
            return eventRepository.save(billEvent);
        } else {
            return null;
        }
    }

    //delete this method
    @Override
    public BillEvent changeStatusBill(BigInteger billId) {
        Event event = eventRepository.findOne(billId);
        BillEvent billEvent = (BillEvent) event;
        if (billEvent == null) {
            return null;
        }
        if (billEvent.isProcessed()) {
            billEvent.setProcessed(false);
            billEvent.setBlockedAmount(0);
        } else {
            billEvent.setProcessed(true);
        }
        return eventRepository.save(billEvent);
    }

    @Override
    public Boolean getStatusBill(BigInteger userId, BigInteger billId) {
        User currentUser = userRepository.findOne(userId);
        BillEvent billEvent = (BillEvent) eventRepository.findOne(billId);
        if (currentUser == null || billEvent == null) {
            return null;
        }
        if (userRepository.getAddressData(currentUser, billEvent.getAddress(), billEvent.getApartment()) != null) {
            return billEvent.isProcessed();
        } else {
            return null;
        }
    }

    @Override
    public BillEvent payPalSingleBillOfApartment(BigInteger userId, BigInteger billId, Integer partAmount) {
        BillEvent billEvent = (BillEvent) eventRepository.findOne(billId);
        if (billEvent == null || billEvent.isSettled()) return null;
        User currentUser = userRepository.findOne(userId);
        AddressData addressData = userRepository.getAddressData(currentUser, billEvent.getAddress(), billEvent.getApartment());
        if (addressData.getAddress().equals(billEvent.getAddress()) && addressData.getApartment().equals(billEvent.getApartment())) {
            if (billEvent.getBalanceBill() == null) billEvent.setBalanceBill(billEvent.getBill());
            if (billEvent.getBalanceBill() <= partAmount) {
                billEvent.setSettled(true);
                partAmount = billEvent.getBalanceBill();
            }
            billEvent.setBalanceBill(billEvent.getBalanceBill() - partAmount);
            HousematePayPalPaymentEvent housematePayPalPaymentEvent = new HousematePayPalPaymentEvent(LocalDateTime.now(), billEvent.getAddress(), billEvent.getApartment(), partAmount, currentUser);
            housematePayPalPaymentEvent.getListSettledBills().add(billEvent);
            housematePayPalPaymentEvent = eventRepository.save(housematePayPalPaymentEvent);
            billEvent.getListHousematePayment().add(housematePayPalPaymentEvent);

            Address address = addressData.getAddress();
            address.setFundAddress(address.getFundAddress() + partAmount);
            address.setAccountBalance(address.getAccountBalance() + partAmount);
            addressRepository.save(address);
            billEvent.setProcessed(false);
            billEvent.getListHousematePayment().sort((o1, o2) -> o2.getDateEvent().compareTo(o1.getDateEvent()));
            return eventRepository.save(billEvent);
        }
        return null;
    }

    @Override
    public HousematePayPalPaymentEvent payPalAllBillsOfApartment(BigInteger userId, BigInteger addressId, String apartment) {
        ObjectId objectAddressId = convertBigIntegerToObjectId(addressId);
        User currentUser = userRepository.findOne(userId);
        if (objectAddressId == null) {
            return null;
        }
        Address address = addressRepository.findOne(addressId);
        Integer amount = 0;
        List<BillEvent> listUnsettledBills = eventRepository.getAmountDebtOfApartment(objectAddressId, apartment);
        for (AddressData ad : userRepository.findOne(userId).getAddressDataList()) {
            if (ad.getAddress().equals(address) && ad.getApartment().equals(apartment)) {
                for (BillEvent billEvent : listUnsettledBills) {
                    billEvent.setSettled(true);
                    eventRepository.save(billEvent);
                    address.setFundAddress(address.getFundAddress() + billEvent.getBalanceBill());
                    address.setAccountBalance(address.getAccountBalance() + billEvent.getBalanceBill());
                    amount += billEvent.getBalanceBill();
                }
                addressRepository.save(address);
                HousematePayPalPaymentEvent housematePayPalPaymentEvent = new HousematePayPalPaymentEvent(LocalDateTime.now(), address, apartment, amount, currentUser);
                housematePayPalPaymentEvent.setListSettledBills(listUnsettledBills);
                return eventRepository.save(housematePayPalPaymentEvent);
            }
        }
        return null;
    }

    @Override
    public BillEvent payCashSingleBillOfApartment(BigInteger managerId, BigInteger addressId, String apartment, BigInteger billId, Integer partAmount) {
        User managerUser = userRepository.findOne(managerId);
        Address address = addressRepository.findOne(addressId);
        BillEvent billEvent = (BillEvent) eventRepository.findOne(billId);
        if (billEvent.isSettled()) return null;
        if (userRepository.getRoleByAddress(managerUser, address) == Role.MANAGER && billEvent.getApartment().equals(apartment)) {
            if (billEvent.getBalanceBill() == null) billEvent.setBalanceBill(billEvent.getBill());
            if (billEvent.getBalanceBill() <= partAmount) {
                billEvent.setSettled(true);
                partAmount = billEvent.getBalanceBill();
            }
            billEvent.setBalanceBill(billEvent.getBalanceBill() - partAmount);
            HousemateCashPaymentEvent housemateCashPaymentEvent = new HousemateCashPaymentEvent(LocalDateTime.now(), address, apartment, partAmount);
            housemateCashPaymentEvent.getListSettledBills().add(billEvent);
            housemateCashPaymentEvent = eventRepository.save(housemateCashPaymentEvent);
            billEvent.getListHousematePayment().add(housemateCashPaymentEvent);
            address.setFundAddress(address.getFundAddress() + partAmount);
            addressRepository.save(address);
            billEvent.setProcessed(false);
            billEvent.getListHousematePayment().sort((o1, o2) -> o2.getDateEvent().compareTo(o1.getDateEvent()));
            return eventRepository.save(billEvent);
        }
        return null;
    }

    @Override
    public HousemateCashPaymentEvent payCashAllBillsOfApartment(BigInteger managerId, BigInteger addressId, String apartment) {
        User managerUser = userRepository.findOne(managerId);
        Address address = addressRepository.findOne(addressId);
        Integer amount = 0;
        ObjectId objectAddressId = convertBigIntegerToObjectId(addressId);
        if (objectAddressId == null) {
            return null;
        }
        List<BillEvent> listUnsettledBills = eventRepository.getAmountDebtOfApartment(objectAddressId, apartment);
        if (userRepository.getRoleByAddress(managerUser, address) == Role.MANAGER) {
            for (BillEvent billEvent : listUnsettledBills) {
                billEvent.setSettled(true);
                eventRepository.save(billEvent);
                amount += billEvent.getBalanceBill();
            }
            address.setFundAddress(address.getFundAddress() + amount);
            addressRepository.save(address);
            HousemateCashPaymentEvent housemateCashPaymentEvent = new HousemateCashPaymentEvent(LocalDateTime.now(), address, apartment, amount);
            housemateCashPaymentEvent.setListSettledBills(listUnsettledBills);
            return eventRepository.save(housemateCashPaymentEvent);
        }
        return null;
    }

    @Override
    public ManagerPaymentEvent addSinglePayment(BigInteger userId, SingleManagerPayment singleManagerPayment) {
        User currentUser = userRepository.findOne(userId);
        BigInteger addressId = singleManagerPayment.getAddressId();
        Address address = addressRepository.findOne(addressId);
        AddressData addressData = userRepository.getAddressDataByAddress(currentUser, address);
        if (Role.MANAGER.equals(addressData.getRole())) {
            //add ManagerPaymentEvent
            ManagerPaymentEvent paymentEvent = eventRepository.save(new ManagerPaymentEvent(singleManagerPayment.getTitle(),
                    LocalDateTime.now(), address, currentUser, singleManagerPayment.getDescription(), singleManagerPayment.getTotalAmount(), singleManagerPayment.isFromFundAddress()));
            if (address.getFundAddress() == null) {
                address.setFundAddress(0 - singleManagerPayment.getTotalAmount());
            } else {
                address.setFundAddress(address.getFundAddress() - singleManagerPayment.getTotalAmount());
            }
            addressRepository.update(address);
            if (singleManagerPayment.isFromFundAddress()) {
                return paymentEvent;
            } else {
                int singleBillAmount = singleManagerPayment.getTotalAmount() / address.getListOfApartment().size();
                for (String apartment : address.getListOfApartment()) {
                    addSingleBill(address, apartment, singleBillAmount, paymentEvent);
                }
                return paymentEvent; //no need to return SingleBillEvent
            }
        }
        return null;
    }

    private void addSingleBill(Address address, String apartment, int amount, ManagerPaymentEvent paymentEvent) {
        eventRepository.save(new SingleBillEvent(LocalDateTime.now(), address, apartment, amount, false, paymentEvent));
    }

    @Override
    public MonthlyBillEvent createMonthlyBillForAddress(BigInteger addressId, BigInteger managerId) {
        Address currentAddress = addressRepository.findOne(addressId);
        User currentUser = userRepository.findOne(managerId);
        if (userRepository.getRoleByAddress(currentUser, currentAddress).equals(Role.MANAGER) && currentAddress.isManagerExist()) {
            currentAddress.setLatestGeneratedMonthlyFee(LocalDateTime.now());
            for (String apartment : currentAddress.getListOfApartment()) {
                eventRepository.save(new MonthlyBillEvent(LocalDateTime.now(), currentAddress, apartment, currentAddress.getMonthlyFee(), false));
            }
        } else {
            return null;
        }
        addressRepository.update(currentAddress);
        return new MonthlyBillEvent(LocalDateTime.now(), currentAddress, null, currentAddress.getMonthlyFee(), false);
    }

    @Override
    public Address receiveMoneyFromAccountByManager(BigInteger managerId, BigInteger addressId, Integer amount, BankAccount bankAccount) {
        User manager = userRepository.findOne(managerId);
        Address address = addressRepository.findOne(addressId);
        if (Role.MANAGER.equals(userRepository.getRoleByAddress(manager, address))) {
            if (bankAccount.getAccount() == null || bankAccount.getBranch() == null || bankAccount.getBank() == null || amount > address.getAccountBalance()) {
                return null;
            }
            address.setAmountForWithdrawal(amount);
            address = addressRepository.save(address);
            manager.setBankAccount(bankAccount);
            manager = userRepository.save(manager);
            emailService.sendNoticeAboutRequestForWithdrawalMoney(manager, address, amount);
            return address;
        }
        return null;
    }

    @Override
    public Address confirmWithdrawalMoney(BigInteger addressId, Integer amount) {
        Address address = addressRepository.findOne(addressId);
        if (Objects.equals(address.getAmountForWithdrawal(), amount))
        {
            address.setAccountBalance(address.getAccountBalance() - address.getAmountForWithdrawal());
            address.setAmountForWithdrawal(0);
            address = addressRepository.save(address);
            return address;
        }
        return null;
    }

    //@Scheduled(cron = "0 0 12 1 * ?")
    @Override
    public void createMonthlyBillForAllApartment() {
        LocalDateTime currentDate = LocalDateTime.now();
        List<Address> allAddress = addressRepository.findAll();
        allAddress.stream().filter(Address::isManagerExist).filter(address -> address.getMonthlyFee() != null).forEach(address -> {
            for (String apartment : address.getListOfApartment()) {
                eventRepository.save(new MonthlyBillEvent(currentDate, address, apartment, address.getMonthlyFee(), false));
            }
        });
    }

    public ObjectId convertBigIntegerToObjectId(BigInteger addressId) {
        ObjectId objectAddressId;
        try {
            objectAddressId = new ObjectId(addressId.toString(16));
        } catch (IllegalArgumentException e) {
            return null;
        }
        return objectAddressId;
    }
}
