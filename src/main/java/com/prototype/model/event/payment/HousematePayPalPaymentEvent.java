package com.prototype.model.event.payment;

import com.prototype.model.Address;
import com.prototype.model.event.ApartmentEvent;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Event")
public class HousematePayPalPaymentEvent extends HousematePaymentEvent {
    public HousematePayPalPaymentEvent(LocalDateTime dateEvent, Address address, String apartment) {
        super(dateEvent, address, apartment);
    }
}
