package com.prototype.model.event.payment;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.prototype.model.Address;
import com.prototype.model.event.ApartmentEvent;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class BillEvent extends ApartmentEvent {
    private Integer bill;
    private Integer balanceBill;
    private boolean settled;
    private boolean processed;

    @DBRef(lazy = true)
    @JsonManagedReference
    private List<HousematePaymentEvent> listHousematePayment;

    public BillEvent(LocalDateTime dateEvent, Address address, String apartment, Integer bill, boolean settled) {
        super(dateEvent, address, apartment);
        this.bill = bill;
        this.balanceBill = bill;
        this.settled = settled;
        this.listHousematePayment = new ArrayList<>();
        setProcessed(false);
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

    public Integer getBalanceBill() {
        return balanceBill;
    }

    public void setBalanceBill(Integer balanceBill) {
        this.balanceBill = balanceBill;
    }

    public List<HousematePaymentEvent> getListHousematePayment() {
        return listHousematePayment;
    }

    public void setListHousematePayment(List<HousematePaymentEvent> listHousematePayment) {
        this.listHousematePayment = listHousematePayment;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
