package com.prototype.web.payment;

import com.prototype.model.event.announcement.UserAnnouncementEvent;
import com.prototype.repository.event.EventRepository;
import com.prototype.repository.user.UserRepository;
import com.prototype.service.payment.PaymentService;
import com.prototype.to.paypal.PayPalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;


@RestController
@RequestMapping(value = PayPalRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class PayPalRestController {
    static final String REST_URL = "/api/v1/sale";

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @PostMapping(value = "/completed", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> payPalSuccessResponse(@RequestBody PayPalResponse payPalResponse) {
        String[] array = payPalResponse.getResource().getInvoiceNumber().split("-");
        BigInteger billId = new BigInteger(array[0]);
        BigInteger userId = new BigInteger(payPalResponse.getResource().getCustom());
        int amount = (int) (Double.parseDouble(payPalResponse.getResource().getAmount().getTotal()) * 100);
        //UserAnnouncementEvent userAnnouncementEvent = new UserAnnouncementEvent(String.valueOf(amount), null, null, userRepository.findOne(userId), billId.toString());
        //eventRepository.save(userAnnouncementEvent);
        paymentService.payPalSingleBillOfApartment(userId, billId, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/denied", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> payPalOtherResponse(@RequestBody PayPalResponse payPalResponse) {
        String[] array = payPalResponse.getResource().getInvoiceNumber().split("-");
        BigInteger billId = new BigInteger(array[0]);
        BigInteger userId = new BigInteger(payPalResponse.getResource().getCustom());
        int amount = (int) (Double.parseDouble(payPalResponse.getResource().getAmount().getTotal()) * 100);
        UserAnnouncementEvent userAnnouncementEvent = new UserAnnouncementEvent(String.valueOf(amount), null, null, userRepository.findOne(userId), billId.toString());
        eventRepository.save(userAnnouncementEvent);
        paymentService.changeStatusBill(billId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
