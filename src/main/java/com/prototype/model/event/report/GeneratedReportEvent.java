package com.prototype.model.event.report;

import com.prototype.model.Address;
import com.prototype.model.event.payment.ManagerPaymentEvent;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.List;

public class GeneratedReportEvent extends ReportEvent{
    @DBRef
    private List<ManagerPaymentEvent> listManagerPayments;

    public GeneratedReportEvent(LocalDateTime dateEvent, Address address) {
        super(dateEvent, address);
    }
    public List<ManagerPaymentEvent> getListManagerPayments() {
        return listManagerPayments;
    }

    public void setListManagerPayments(List<ManagerPaymentEvent> listManagerPayments) {
        this.listManagerPayments = listManagerPayments;
    }
}
