package com.prototype.model.event.payment;

import com.prototype.model.Address;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Event")
public class MonthlyBillEvent extends BillEvent {

    public MonthlyBillEvent(LocalDateTime dateEvent, Address address, String apartment, Integer bill, boolean settled) {
        super(dateEvent, address, apartment, bill, settled);
    }
}
