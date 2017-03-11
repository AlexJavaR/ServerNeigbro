package com.prototype.web.payment;

import com.prototype.model.event.ApartmentEvent;
import com.prototype.model.event.payment.BillEvent;
import com.prototype.model.event.payment.HousematePayPalPaymentEvent;
import com.prototype.security.AuthorizedUser;
import com.prototype.service.payment.PaymentService;
import com.prototype.to.BillForStatusInProcess;
import com.prototype.to.HousemateBillPayment;
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
@RequestMapping(value = HousemateRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class HousemateRestController {

    static final String REST_URL = "/api/v1/me";

    @Autowired
    private PaymentService paymentService;

    //Pay amount debt of single apartment with PayPal - DONE
    @PostMapping(value = "/paypal", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HousematePayPalPaymentEvent> payPalAllBillsOfApartment(@RequestBody HousemateBillPayment housemateBillPayment) {
        BigInteger userId = AuthorizedUser.id();
        HousematePayPalPaymentEvent housematePayPalPaymentEvent = paymentService.payPalAllBillsOfApartment(userId, housemateBillPayment.getAddressId(), housemateBillPayment.getApartment());
        if (housematePayPalPaymentEvent == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(housematePayPalPaymentEvent, HttpStatus.OK);
    }

    //Pay single bill of apartment with PayPal - DONE
    //Used class from cash payment - ???
    @PutMapping(value = "/paypal", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BillEvent> payPalSingleBillOfApartment(@RequestBody HousemateBillPayment housemateBillPayment) {
        BigInteger userId = AuthorizedUser.id();
        BillEvent billEvent = paymentService.payPalSingleBillOfApartment(userId, housemateBillPayment.getBillId(), housemateBillPayment.getPartAmount());
        if (billEvent == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(billEvent, HttpStatus.OK);
    }

    //For change bill in process
    @PutMapping(value = "/paypal/bill", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BillEvent> setStatusBillInProcess(@RequestBody BillForStatusInProcess billForStatusInProcess) {
        if (billForStatusInProcess.getBillId() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BigInteger userId = AuthorizedUser.id();
        BillEvent billEvent = paymentService.setStatusBillInProcess(userId, billForStatusInProcess.getBillId());
        if (billEvent == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(billEvent, HttpStatus.OK);
    }

    //For change bill in process
    @GetMapping(value = "/change/{billId}")
    public ResponseEntity<BillEvent> setStatusBill(@PathVariable("billId") BigInteger billId) {
        BigInteger userId = AuthorizedUser.id();
        if (billId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BillEvent billEvent = paymentService.changeStatusBill(billId);
        if (billEvent == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(billEvent, HttpStatus.OK);
    }

    //For change bill in process
    @GetMapping(value = "/status/{billId}")
    public ResponseEntity<Boolean> getStatusBill(@PathVariable("billId") BigInteger billId) {
        BigInteger userId = AuthorizedUser.id();
        if (billId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Boolean isProcess = paymentService.getStatusBill(userId, billId);
        if (isProcess == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(isProcess, HttpStatus.OK);
    }

    //Get amount debt and all bills of single apartment - DONE
    @GetMapping(value = "/bills/{addressId}/{apartment}")
    public ResponseEntity<Map<Integer, List<ApartmentEvent>>> findAllBillsOfApartment(@PathVariable("addressId") BigInteger addressId,
                                                                                      @PathVariable("apartment") String apartment) {
        BigInteger userId = AuthorizedUser.id();
        List<ApartmentEvent> listBills = paymentService.findAllBillsOfApartment(userId, addressId, apartment);
        Integer amountDebt = paymentService.getAmountDebtOfApartment(userId, addressId, apartment);
        if (listBills == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (listBills.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Map<Integer, List<ApartmentEvent>> debtOfApartment = new HashMap<>();
        debtOfApartment.put(amountDebt, listBills);
        return new ResponseEntity<>(debtOfApartment, HttpStatus.OK);
    }

    //Get amount debt of current apartment - DONE
    @GetMapping(value = "/bills/debt/{addressId}/{apartment}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getAmountDebtOfApartment(@PathVariable("addressId") BigInteger addressId,
                                                           @PathVariable("apartment") String apartment) {
        BigInteger userId = AuthorizedUser.id();
        Integer amountDebt = paymentService.getAmountDebtOfApartment(userId, addressId, apartment);
        return new ResponseEntity<>(amountDebt, HttpStatus.OK);
    }
}
