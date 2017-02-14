package com.prototype.model.event.payment;

import com.prototype.model.Address;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Event")
public class SingleBillEvent extends BillEvent {

    @DBRef
    private ManagerPaymentEvent managerPaymentEvent;

    public SingleBillEvent(LocalDateTime dateEvent, Address address, String apartment, Integer bill, boolean settled, ManagerPaymentEvent managerPaymentEvent) {
        super(dateEvent, address, apartment, bill, settled);
        this.managerPaymentEvent = managerPaymentEvent;
    }
}
