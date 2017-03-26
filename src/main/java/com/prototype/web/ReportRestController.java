package com.prototype.web;

import com.prototype.model.event.report.GeneratedReportEvent;
import com.prototype.model.event.report.ReportEvent;
import com.prototype.security.AuthorizedUser;
import com.prototype.service.report.ReportService;
import com.prototype.to.HousemateCurrentReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping(value = ReportRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportRestController {
    static final String REST_URL = "/api/v1";

    @Autowired
    private ReportService reportService;

    @PostMapping(value = "/report", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneratedReportEvent> createCurrentReport(@RequestBody HousemateCurrentReport housemateCurrentReport)
    {
        BigInteger userId = AuthorizedUser.id();
        GeneratedReportEvent generatedReportEvent = reportService.createCurrentReport(userId, housemateCurrentReport.getAddressId());
        if (generatedReportEvent == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(generatedReportEvent, HttpStatus.OK);
    }

    @GetMapping(value = "/report/{addressId}")
    public ResponseEntity<List<ReportEvent>> findAllReportOfAddress(@PathVariable("addressId") BigInteger addressId) {
        BigInteger userId = AuthorizedUser.id();
        if (addressId == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<ReportEvent> reportEventList = reportService.findAllReportOfAddress(addressId, userId);
        if (reportEventList == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (reportEventList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reportEventList, HttpStatus.OK);
    }

    @GetMapping(value = "/testreport")
    public ResponseEntity<Void> findAllReportOfAddress() {
        reportService.createTestMonthlyReport();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
