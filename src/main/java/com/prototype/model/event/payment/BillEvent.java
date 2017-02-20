package com.prototype.model.event.payment;

import com.prototype.model.Address;
import com.prototype.model.event.ApartmentEvent;
import com.prototype.model.event.payment.split.SplitPaymentEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class BillEvent extends ApartmentEvent {
    private Integer bill;
    private Integer balanceBill;
    private boolean settled;
    private List<SplitPaymentEvent> listSplitPayment;

    public BillEvent(LocalDateTime dateEvent, Address address, String apartment, Integer bill, boolean settled) {
        super(dateEvent, address, apartment);
        this.bill = bill;
        this.balanceBill = bill;
        this.settled = settled;
        this.listSplitPayment = new ArrayList<>();
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

    public List<SplitPaymentEvent> getListSplitPayment() {
        return listSplitPayment;
    }

    public void setListSplitPayment(List<SplitPaymentEvent> listSplitPayment) {
        this.listSplitPayment = listSplitPayment;
    }
}
