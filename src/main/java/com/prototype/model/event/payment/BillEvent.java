package com.prototype.model.event.payment;

import com.prototype.model.Address;
import com.prototype.model.event.ApartmentEvent;

import java.time.LocalDateTime;

public abstract class BillEvent extends ApartmentEvent {
    private Integer bill;
    private boolean settled;

    public BillEvent(LocalDateTime dateEvent, Address address, String apartment, Integer bill, boolean settled) {
        super(dateEvent, address, apartment);
        this.bill = bill;
        this.settled = settled;
    }

    public boolean isSettled() {
        return settled;
    }

    public void setSettled(boolean settled) {
        this.settled = settled;
    }

    public Integer getBill() {
        return bill;
    }

    public void setBill(Integer bill) {
        this.bill = bill;
    }

}
