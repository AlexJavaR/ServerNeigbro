package com.prototype.util.exception;

import java.math.BigInteger;

public class ExceptionReport {

    private Device device;
    private String useragent;
    private String stacktrace;
    private BigInteger userId;

    public ExceptionReport() {
    }

    public ExceptionReport(Device device, String useragent, String stacktrace) {
        this.device = device;
        this.useragent = useragent;
        this.stacktrace = stacktrace;
    }

    public ExceptionReport(Device device, String useragent, String stacktrace, BigInteger userId) {
        this.device = device;
        this.useragent = useragent;
        this.stacktrace = stacktrace;
        this.userId = userId;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getUseragent() {
        return useragent;
    }

    public void setUseragent(String useragent) {
        this.useragent = useragent;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }
}
