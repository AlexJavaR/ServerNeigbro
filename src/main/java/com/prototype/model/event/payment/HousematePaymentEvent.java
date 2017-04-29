package com.prototype.model.event.payment;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.prototype.model.Address;
import com.prototype.model.event.ApartmentEvent;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class HousematePaymentEvent extends ApartmentEvent {

    @DBRef(lazy = true)
    @JsonBackReference
    private List<BillEvent> listSettledBills;
    private Integer amount;

    public HousematePaymentEvent(LocalDateTime dateEvent, Address address, String apartment, Integer amount) {
        super(dateEvent, address, apartment);
        this.listSettledBills = new ArrayList<>();
        this.amount = amount;
        setPersonal(true);
    }

    public List<BillEvent> getListSettledBills() {
        return listSettledBills;
    }

    public void setListSettledBills(List<BillEvent> listSettledBills) {
        this.listSettledBills = listSettledBills;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
