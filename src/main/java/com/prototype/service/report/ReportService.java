package com.prototype.service.report;

import com.prototype.model.event.report.GeneratedReportEvent;
import com.prototype.model.event.report.ReportEvent;
import com.prototype.model.event.report.UploadReportEvent;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {
    ReportEvent createReport(BigInteger userId, BigInteger addressId, LocalDateTime startDate, LocalDateTime endDate);

    List<ReportEvent> findAllReportOfAddress(BigInteger addressId, BigInteger userId);

    GeneratedReportEvent createCurrentReport(BigInteger userId, BigInteger addressId);

    List<UploadReportEvent> findAllUploadedReportsOfAddress(BigInteger addressId, BigInteger userId);

    File findCurrentUploadedReportsOfAddress(BigInteger addressId, BigInteger userId, BigInteger reportId);

    UploadReportEvent createEmptyUploadedReportEvent(UploadReportEvent uploadReportEvent);

    void createMonthlyReport();

    void createTestMonthlyReport();

    UploadReportEvent uploadFileForUploadReportEvent(BigInteger userId, BigInteger addressId, MultipartFile uploadedFile);

    void deleteCurrentUploadedReportAndFile(BigInteger userId, BigInteger addressId, BigInteger reportId);
}
