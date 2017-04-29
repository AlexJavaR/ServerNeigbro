package com.prototype.util.exception;

import com.prototype.service.exception.ExceptionReportSenderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @Autowired
    private ExceptionReportSenderImpl sender;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
        // make stacktrace a string
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();

        // send report to developer
        ExceptionReport report = new ExceptionReport();
        report.setDevice(Device.SERVER);
        report.setUseragent("Back-end server");
        report.setStacktrace(exceptionAsString);
        sender.sendExceptionReport(report);

        // send response to client
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setMessage("Please contact the administrator");
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }
}
