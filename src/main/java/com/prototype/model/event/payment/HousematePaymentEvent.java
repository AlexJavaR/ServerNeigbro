package com.prototype.model.event.payment;

import com.prototype.model.Address;
import com.prototype.model.event.ApartmentEvent;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class HousematePaymentEvent extends ApartmentEvent {

    @DBRef
    private List<BillEvent> listSettledBills;

    public HousematePaymentEvent(LocalDateTime dateEvent, Address address, String apartment) {
        super(dateEvent, address, apartment);
        this.listSettledBills = new ArrayList<>();
    }

    public List<BillEvent> getListSettledBills() {
        return listSettledBills;
    }

    public void setListSettledBills(List<BillEvent> listSettledBills) {
        this.listSettledBills = listSettledBills;
    }
}
