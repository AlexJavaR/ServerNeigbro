package com.prototype.web.payment;

import com.prototype.model.Address;
import com.prototype.model.event.ApartmentEvent;
import com.prototype.model.event.payment.BillEvent;
import com.prototype.model.event.payment.HousemateCashPaymentEvent;
import com.prototype.model.event.payment.ManagerPaymentEvent;
import com.prototype.model.event.payment.MonthlyBillEvent;
import com.prototype.security.AuthorizedUser;
import com.prototype.service.address.AddressService;
import com.prototype.service.payment.PaymentService;
import com.prototype.to.ApartmentsWithDebt;
import com.prototype.to.HousemateBillPayment;
import com.prototype.to.SingleManagerPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = ManagerRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ManagerRestController {

    static final String REST_URL = "/api/v1";

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AddressService addressService;

    @GetMapping(value = "/monthly")
    public ResponseEntity<MonthlyBillEvent> createMonthleBills() {
        paymentService.createMonthlyBillForAllApartment();

        return new ResponseEntity<>(HttpStatus.OK);
    }


    //Get all ManagerPaymentEvent of current address
    @GetMapping(value = "/payment/{addressId}")
    public ResponseEntity<List<ManagerPaymentEvent>> findAllManagerPaymentEvent(@PathVariable("addressId") BigInteger addressId) {
        BigInteger managerId = AuthorizedUser.id();
        List<ManagerPaymentEvent> managerPaymentEventList = paymentService.findAllManagerPaymentEvent(managerId, addressId);

        if (managerPaymentEventList == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (managerPaymentEventList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(managerPaymentEventList, HttpStatus.OK);
    }

    //Add single manager payment to address - DONE
    @PostMapping(value = "/me/payment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ManagerPaymentEvent> addSingleManagerPayment(@RequestBody SingleManagerPayment singleManagerPayment) {
        //TODO validate new single payment
        BigInteger userId = AuthorizedUser.id();

        ManagerPaymentEvent managerPaymentEvent = paymentService.addSinglePayment(userId, singleManagerPayment);
        if (managerPaymentEvent == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(managerPaymentEvent, HttpStatus.CREATED);
    }

    //Pay all unsettled bills of single apartment with cash - Role.MANAGER
    @PostMapping(value = "/cash", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HousemateCashPaymentEvent> payCashAllBillsOfApartment(@RequestBody HousemateBillPayment housemateBillPayment) {
        BigInteger managerId = AuthorizedUser.id();
        HousemateCashPaymentEvent housemateCashPaymentEvent = paymentService.payCashAllBillsOfApartment(managerId, housemateBillPayment.getAddressId(), housemateBillPayment.getApartment());
        if (housemateCashPaymentEvent == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(housemateCashPaymentEvent, HttpStatus.OK);
    }

    //Pay single bill of apartment with cash - DONE
    @PutMapping(value = "/cash", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BillEvent> payCashSingleBillOfApartment(@RequestBody HousemateBillPayment housemateBillPayment) {
        BigInteger managerId = AuthorizedUser.id();
        BillEvent billEvent = paymentService.payCashSingleBillOfApartment(managerId, housemateBillPayment.getAddressId(),
                housemateBillPayment.getApartment(), housemateBillPayment.getBillId(), housemateBillPayment.getPartAmount());
        if (billEvent == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(billEvent, HttpStatus.OK);
    }

    //Get amount debt and all bills of single apartment - DONE
    @GetMapping(value = "/bills/{addressId}/{apartment}")
    public ResponseEntity<Map<Integer, List<ApartmentEvent>>> findAllBillsOfApartmentByManager(@PathVariable("addressId") BigInteger addressId,
                                                                                               @PathVariable("apartment") String apartment) {
        BigInteger managerId = AuthorizedUser.id();
        List<ApartmentEvent> listBills = paymentService.findAllBillsOfApartmentByManager(managerId, addressId, apartment);
        Integer amountDebt = paymentService.getAmountDebtOfApartmentByManager(managerId, addressId, apartment);
        if (listBills == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (listBills.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Map<Integer, List<ApartmentEvent>> debtOfApartment = new HashMap<>();
        debtOfApartment.put(amountDebt, listBills);
        return new ResponseEntity<>(debtOfApartment, HttpStatus.OK);
    }

    //Get amount debt for all apartments - DONE
    @GetMapping(value = "/bills/{addressId}")
    public ResponseEntity<List<ApartmentsWithDebt>> getDebtOfAllApartmentsByManager(@PathVariable("addressId") BigInteger addressId) {
        BigInteger managerId = AuthorizedUser.id();
        if (addressId == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Address address = addressService.findOne(addressId);
        if (address == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<ApartmentsWithDebt> apartmentsWithDebt = paymentService.getDebtOfAllApartmentsByManager(managerId, addressId);
        if (apartmentsWithDebt == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (apartmentsWithDebt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(apartmentsWithDebt, HttpStatus.OK);
    }
}
