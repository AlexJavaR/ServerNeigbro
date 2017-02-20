package com.prototype.service.payment;

import com.prototype.model.Address;
import com.prototype.model.AddressData;
import com.prototype.model.Role;
import com.prototype.model.User;
import com.prototype.model.event.ApartmentEvent;
import com.prototype.model.event.payment.*;
import com.prototype.model.event.payment.split.CashSplitPaymentEvent;
import com.prototype.model.event.payment.split.PayPalSplitPaymentEvent;
import com.prototype.model.event.payment.split.SplitPaymentEvent;
import com.prototype.repository.address.AddressRepository;
import com.prototype.repository.event.EventRepository;
import com.prototype.repository.user.UserRepository;
import com.prototype.to.ApartmentsWithDebt;
import com.prototype.to.SingleManagerPayment;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("PaymentService")
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private UserDao userDao;

    @Override
    public List<ApartmentEvent> findAllBillsOfApartment(BigInteger userId, BigInteger addressId) {
        ObjectId objectAddressId = new ObjectId(addressId.toString(16));
        User currentUser = userRepository.findOne(userId);
        String apartment = currentUser.getApartment(addressId);
        if (apartment == null) {
            return null;
        }
        return eventRepository.findAllPersonalEventOfApartment(objectAddressId, apartment);
    }

    @Override
    public List<ApartmentEvent> findAllBillsOfApartmentByManager(BigInteger managerId, BigInteger addressId, String apartment) {
        ObjectId objectAddressId = new ObjectId(addressId.toString(16));
        User managerUser = userRepository.findOne(managerId);
        if (userRepository.getRoleByAddress(managerUser, addressRepository.findOne(addressId)) == Role.MANAGER) {
            return eventRepository.findAllPersonalEventOfApartment(objectAddressId, apartment);
        }
        return null;
    }


    @Override
    public List<ManagerPaymentEvent> findAllManagerPaymentEvent(BigInteger managerId, BigInteger addressId) {
        ObjectId objectAddressId = new ObjectId(addressId.toString(16));
        User managerUser = userRepository.findOne(managerId);
        if (userRepository.getRoleByAddress(managerUser, addressRepository.findOne(addressId)) == Role.MANAGER) {
            return eventRepository.findAllManagerPaymentEvent(objectAddressId);
        }
        return null;
    }

    @Override
    public Integer getAmountDebtOfApartment(BigInteger userId, BigInteger addressId) {
        ObjectId objectAddressId = new ObjectId(addressId.toString(16));
        User currentUser = userRepository.findOne(userId);
        String apartment = currentUser.getApartment(addressId);
        if (apartment == null) {
            return null;
        }
        List<BillEvent> unsettledBills = eventRepository.getAmountDebtOfApartment(objectAddressId, apartment);
        Integer amountDebt = 0;
        for (BillEvent bill : unsettledBills) {
            amountDebt += bill.getBill();
        }
        return amountDebt;
    }

    @Override
    public Integer getAmountDebtOfApartmentByManager(BigInteger managerId, BigInteger addressId, String apartment) {
        ObjectId objectAddressId = new ObjectId(addressId.toString(16));
        User managerUser = userRepository.findOne(managerId);
        if (userRepository.getRoleByAddress(managerUser, addressRepository.findOne(addressId)) == Role.MANAGER) {
            List<BillEvent> unsettledBills = eventRepository.getAmountDebtOfApartment(objectAddressId, apartment);
            Integer amountDebt = 0;
            for (BillEvent bill : unsettledBills) {
                amountDebt += bill.getBill();
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
    public BillEvent payPalSingleBillOfApartment(BigInteger userId, BigInteger billId, Integer partAmount) {
        BillEvent billEvent = (BillEvent) eventRepository.findOne(billId);
        if (billEvent.isSettled()) return null;
        User currentUser = userRepository.findOne(userId);
        boolean addSplitPayment = false;
        AddressData addressData = userRepository.getAddressDataByAddress(currentUser, billEvent.getAddress());
        if (addressData.getAddress().equals(billEvent.getAddress()) && addressData.getApartment().equals(billEvent.getApartment())) {
            if (billEvent.getBalanceBill() <= partAmount) {
                billEvent.setSettled(true);
                partAmount = billEvent.getBalanceBill();
            }
            if (!billEvent.getListSplitPayment().isEmpty()) {
                for (SplitPaymentEvent payment : billEvent.getListSplitPayment()) {
                    if (payment.getAuthor().getId().equals(userId)) {
                        payment.setAmount(payment.getAmount() + partAmount);
                        addSplitPayment = true;
                        billEvent.setBalanceBill(billEvent.getBalanceBill() - partAmount);
                    }
                }
//                billEvent.getListSplitPayment().stream().filter(payment -> payment.getAuthor().getId().equals(userId))
//                        .forEach(payment -> payment.setAmount(payment.getAmount() + finalPartAmount));
            }
            if (!addSplitPayment) {
                PayPalSplitPaymentEvent payPalSplitPaymentEvent = new PayPalSplitPaymentEvent(LocalDateTime.now(), currentUser, partAmount);
                billEvent.getListSplitPayment().add(payPalSplitPaymentEvent);
                billEvent.setBalanceBill(billEvent.getBalanceBill() - partAmount);
            }

            HousematePayPalPaymentEvent housematePayPalPaymentEvent = new HousematePayPalPaymentEvent(LocalDateTime.now(),
                    addressData.getAddress(), addressData.getApartment(), partAmount);
            housematePayPalPaymentEvent.getListSettledBills().add(billEvent);
            eventRepository.save(housematePayPalPaymentEvent);

            Address address = addressData.getAddress();
            address.setFundAddress(address.getFundAddress() + partAmount);
            address.setAccountBalance(address.getAccountBalance() + partAmount);
//                if (billEvent.getClass().getSimpleName().equals("SingleBillEvent")) {
//                    address.setAccountBalance(address.getAccountBalance() + billEvent.getBill());
//                } else if (billEvent.getClass().getSimpleName().equals("MonthlyBillEvent")) {
//                    address.setFundAddress(address.getFundAddress() + billEvent.getBill());
//                }
            addressRepository.save(address);
            return eventRepository.save(billEvent);
        }
        return null;
    }

    @Override
    public HousematePayPalPaymentEvent payPalAllBillsOfApartment(BigInteger userId, BigInteger addressId, String apartment) {
        ObjectId objectAddressId = new ObjectId(addressId.toString(16));
        Address address = addressRepository.findOne(addressId);
        Integer amount = 0;
        List<BillEvent> listUnsettledBills = eventRepository.getAmountDebtOfApartment(objectAddressId, apartment);
        for (AddressData ad : userRepository.findOne(userId).getAddressDataList()) {
            if (ad.getAddress().equals(address) && ad.getApartment().equals(apartment)) {
                for (BillEvent billEvent : listUnsettledBills) {
                    billEvent.setSettled(true);
                    eventRepository.save(billEvent);
                    address.setFundAddress(address.getFundAddress() + billEvent.getBill());
                    address.setAccountBalance(address.getAccountBalance() + billEvent.getBill());
                    amount += billEvent.getBill();
                }
                addressRepository.save(address);
                HousematePayPalPaymentEvent housematePayPalPaymentEvent = new HousematePayPalPaymentEvent(LocalDateTime.now(), address, apartment, amount);
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
            if (billEvent.getBalanceBill() <= partAmount) {
                billEvent.setSettled(true);
                partAmount = billEvent.getBalanceBill();
            }
            CashSplitPaymentEvent cashSplitPaymentEvent = new CashSplitPaymentEvent(LocalDateTime.now(), null, partAmount);
            billEvent.getListSplitPayment().add(cashSplitPaymentEvent);
            billEvent.setBalanceBill(billEvent.getBalanceBill() - partAmount);
            eventRepository.save(billEvent);
            address.setFundAddress(address.getFundAddress() + partAmount);
            addressRepository.save(address);
            HousemateCashPaymentEvent housemateCashPaymentEvent = new HousemateCashPaymentEvent(LocalDateTime.now(), address, apartment, partAmount);
            housemateCashPaymentEvent.getListSettledBills().add(billEvent);
            eventRepository.save(housemateCashPaymentEvent);
            return billEvent;
        }
        return null;
    }

    @Override
    public HousemateCashPaymentEvent payCashAllBillsOfApartment(BigInteger managerId, BigInteger addressId, String apartment) {
        User managerUser = userRepository.findOne(managerId);
        Address address = addressRepository.findOne(addressId);
        Integer amount = 0;
        ObjectId objectAddressId = new ObjectId(addressId.toString(16));
        List<BillEvent> listUnsettledBills = eventRepository.getAmountDebtOfApartment(objectAddressId, apartment);
        if (userRepository.getRoleByAddress(managerUser, address) == Role.MANAGER) {
            for (BillEvent billEvent : listUnsettledBills) {
                billEvent.setSettled(true);
                eventRepository.save(billEvent);
                amount += billEvent.getBill();
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

    @Scheduled(cron = "0 0 12 1 * ?")
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
}
