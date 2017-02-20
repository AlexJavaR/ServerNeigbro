package com.prototype.model.event.payment;

import com.prototype.model.Address;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Event")
public class HousemateCashPaymentEvent extends HousematePaymentEvent {
    public HousemateCashPaymentEvent(LocalDateTime dateEvent, Address address, String apartment, Integer amount) {
        super(dateEvent, address, apartment, amount);
    }
}
