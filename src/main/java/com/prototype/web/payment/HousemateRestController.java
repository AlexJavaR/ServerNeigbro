package com.prototype.web.payment;

import com.prototype.model.event.ApartmentEvent;
import com.prototype.model.event.payment.BillEvent;
import com.prototype.model.event.payment.HousematePayPalPaymentEvent;
import com.prototype.security.AuthorizedUser;
import com.prototype.service.payment.PaymentService;
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

    //Get amount debt and all bills of single apartment - DONE
    @GetMapping(value = "/bills/{addressId}")
    public ResponseEntity<Map<Integer, List<ApartmentEvent>>> findAllBillsOfApartment(@PathVariable("addressId") BigInteger addressId) {
        BigInteger userId = AuthorizedUser.id();
        List<ApartmentEvent> listBills = paymentService.findAllBillsOfApartment(userId, addressId);
        Integer amountDebt = paymentService.getAmountDebtOfApartment(userId, addressId);
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
    //@GetMapping(value = "/debt/{addressId}", produces = MediaType.APPLICATION_JSON_VALUE)
    //public ResponseEntity<Double> getAmountDebtOfApartment(@PathVariable("addressId") BigInteger addressId) {
    //    BigInteger userId = AuthorizedUser.id();
    //    Double amountDebt = paymentService.getAmountDebtOfApartment(userId, addressId);
    //    return new ResponseEntity<>(amountDebt, HttpStatus.OK);
    //}
}
