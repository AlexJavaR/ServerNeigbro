package com.prototype.model.event.report;

import com.prototype.model.Address;
import com.prototype.model.event.AddressEvent;
import com.prototype.model.event.payment.ManagerPaymentEvent;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class ReportEvent extends AddressEvent {


    public ReportEvent(LocalDateTime dateEvent, Address address) {
        super(dateEvent, address);

    }


}
