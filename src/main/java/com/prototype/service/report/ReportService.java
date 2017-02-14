package com.prototype.service.report;

import com.prototype.model.event.report.GeneratedReportEvent;
import com.prototype.model.event.report.ReportEvent;
import com.prototype.model.event.report.UploadReportEvent;

import java.io.File;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {
    ReportEvent createReport(BigInteger userId, BigInteger addressId, LocalDateTime startDate, LocalDateTime endDate);

    List<ReportEvent> findAllReportOfAddress(BigInteger addressId, BigInteger userId);

    GeneratedReportEvent createCurrentReport(BigInteger userId, BigInteger addressId);

    UploadReportEvent createUploadReportEvent(BigInteger userId, BigInteger addressId, File file);

    List<UploadReportEvent> findAllUploadedReportsOfAddress(BigInteger addressId, BigInteger userId);

    UploadReportEvent findCurrentUploadedReportsOfAddress(BigInteger addressId, BigInteger userId, BigInteger textId);

    void deleteCurrentUploadedReport(UploadReportEvent uploadReportEvent);

    UploadReportEvent createEmptyUploadedReportEvent(UploadReportEvent uploadReportEvent);

    void createMonthlyReport();
}
