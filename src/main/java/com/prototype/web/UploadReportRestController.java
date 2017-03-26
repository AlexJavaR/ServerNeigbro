package com.prototype.web;

import com.prototype.model.event.report.UploadReportEvent;
import com.prototype.security.AuthorizedUser;
import com.prototype.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import java.io.*;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping(value = UploadReportRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@MultipartConfig(maxFileSize = -1L, maxRequestSize = -1L, fileSizeThreshold = 10 * 1024 * 1024)
public class UploadReportRestController {
    static final String REST_URL = "/api/v1";

    @Autowired
    private ReportService reportService;

    @PostMapping(value = "/uploadedreport/{addressId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadReportEvent> uploadFile(
            @RequestParam("uploadedFile") MultipartFile uploadedFile, @PathVariable("addressId") BigInteger addressId) {
        if (addressId == null || uploadedFile.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        BigInteger userId = AuthorizedUser.id();
        UploadReportEvent uploadReportEvent = reportService.uploadFileForUploadReportEvent(userId, addressId, uploadedFile);

        return new ResponseEntity<>(uploadReportEvent, HttpStatus.OK);
    }

    @GetMapping(value = "/uploadedreport/{addressId}")
    public ResponseEntity<List<UploadReportEvent>> findAllUploadedReports(@PathVariable("addressId") BigInteger addressId) {
        if (addressId == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        BigInteger userId = AuthorizedUser.id();
        List<UploadReportEvent> uploadReportEvents = reportService.findAllUploadedReportsOfAddress(addressId, userId);
        if (uploadReportEvents == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (uploadReportEvents.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(uploadReportEvents, HttpStatus.OK);
    }


    @GetMapping(value = "/uploadedreport/{addressId}/{reportId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<FileSystemResource> findCurrentUploadedReport(@PathVariable("addressId") BigInteger addressId, @PathVariable("reportId") BigInteger reportId) throws IOException, NullPointerException {
        if (addressId == null || reportId == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        File file = reportService.findCurrentUploadedReportsOfAddress(addressId, AuthorizedUser.id(), reportId);
        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!file.exists()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        FileSystemResource fileSystemResource = new FileSystemResource(file);
        return new ResponseEntity<>(fileSystemResource, HttpStatus.OK);
    }


    @DeleteMapping("/uploadedreport/delete/{addressId}/{reportId}")
    public ResponseEntity<Void> deleteCurrentUploadedReport(@PathVariable("addressId") BigInteger addressId, @PathVariable("reportId") BigInteger reportId) throws IOException {
        if (addressId == null || reportId == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        BigInteger userId = AuthorizedUser.id();
        reportService.deleteCurrentUploadedReportAndFile(userId, addressId, reportId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}












