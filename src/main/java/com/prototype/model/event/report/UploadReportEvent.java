package com.prototype.model.event.report;

import com.prototype.model.Address;

import java.time.LocalDateTime;

public class UploadReportEvent extends ReportEvent {
    private String name;
    private long length;


    public UploadReportEvent(LocalDateTime dateEvent, Address address, String name, long length) {
        super(dateEvent, address);
        this.name = name;
        this.length = length;

    }

    public String getName() {
        return name;
    }
}
