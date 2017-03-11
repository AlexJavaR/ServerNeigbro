package com.prototype.model.event.report;

import com.prototype.model.Address;

import java.time.LocalDateTime;

public class UploadReportEvent extends ReportEvent {
    private String name;
    private String extension;
    private long length;

    public UploadReportEvent(LocalDateTime dateEvent, Address address, String name, String extension, long length) {
        super(dateEvent, address);
        this.name = name;
        this.extension = extension;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }
}
