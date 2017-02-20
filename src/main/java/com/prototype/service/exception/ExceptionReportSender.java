package com.prototype.service.exception;

import com.prototype.util.exception.ExceptionReport;

public interface ExceptionReportSender {

    void sendExceptionReport(ExceptionReport report);
}
