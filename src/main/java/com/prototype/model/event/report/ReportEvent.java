package com.prototype.model.event.report;

import com.prototype.model.Address;
import com.prototype.model.event.AddressEvent;

import java.time.LocalDateTime;

public abstract class ReportEvent extends AddressEvent {
    public ReportEvent(LocalDateTime dateEvent, Address address) {
        super(dateEvent, address);
        setPersonal(false);
    }
}
