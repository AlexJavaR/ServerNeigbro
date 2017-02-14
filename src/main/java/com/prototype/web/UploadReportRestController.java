package com.prototype.web;

import com.prototype.model.Role;
import com.prototype.model.event.report.UploadReportEvent;
import com.prototype.repository.address.AddressRepositoryImpl;
import com.prototype.repository.user.UserRepositoryImpl;
import com.prototype.security.AuthorizedUser;
import com.prototype.service.report.ReportService;
import org.apache.commons.io.FileUtils;
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
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = UploadReportRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@MultipartConfig(maxFileSize = -1L, maxRequestSize = -1L, fileSizeThreshold = 10 * 1024 * 1024)
public class UploadReportRestController {
    static final String REST_URL = "/api/v1";
    private String filePath = "uploadedReports";


    @Autowired
    private ReportService reportService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private AddressRepositoryImpl addressRepository;


    @PostMapping(value = "/uploadedreport/{addressId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadReportEvent> uploadFile(
            @RequestParam("uploadedFile") MultipartFile uploadedFile, @PathVariable("addressId") BigInteger addressId) {
        BigInteger userId = AuthorizedUser.id();
        Role role = userRepository.getRoleByAddress(userRepository.findOne(userId), addressRepository.findOne(addressId));
        if (Role.MANAGER.equals(role)) {
            if (!(uploadedFile.isEmpty())) {
                try {
                    byte[] bytes = uploadedFile.getBytes();
                    File dir = new File(filePath + File.separator + addressId.toString());
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    UploadReportEvent uploadReportEvent = new UploadReportEvent(LocalDateTime.now(), addressRepository.findOne(addressId), uploadedFile.getOriginalFilename(), uploadedFile.getSize());
                    reportService.createEmptyUploadedReportEvent(uploadReportEvent);
                    String name = uploadReportEvent.getId().toString();

                    File file = new File(dir.getAbsolutePath() + File.separator + name);


                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
                    stream.write(bytes);
                    stream.flush();
                    stream.close();
                    return new ResponseEntity<>(uploadReportEvent, HttpStatus.OK);


                } catch (Exception e) {
                    return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.LENGTH_REQUIRED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(value = "/uploadedreport/{addressId}")
    public ResponseEntity<List<UploadReportEvent>> findAllUploadedReports(@PathVariable BigInteger addressId) {
        BigInteger userId = AuthorizedUser.id();
        List<UploadReportEvent> uploadReportEvents = reportService.findAllUploadedReportsOfAddress(addressId, userId);
        if (uploadReportEvents == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        else if (uploadReportEvents.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(uploadReportEvents, HttpStatus.OK);
    }


    @GetMapping(value = "/uploadedreport/{addressId}/{textId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<FileSystemResource> findCurrentUploadedReport(@PathVariable BigInteger addressId, @PathVariable BigInteger textId) throws IOException, NullPointerException {

        UploadReportEvent uploadReportEvent = reportService.findCurrentUploadedReportsOfAddress(addressId, AuthorizedUser.id(), textId);
        if (uploadReportEvent == null)
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);

        File file = FileUtils.getFile(filePath + File.separator + addressId.toString() + File.separator + textId.toString());

        if (!file.exists())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        FileSystemResource fileSystemResource = new FileSystemResource(file);
        return new ResponseEntity<>(fileSystemResource, HttpStatus.OK);
    }


    @DeleteMapping("/uploadedreport/delete/{addressId}/{textId}")
    public ResponseEntity<Void> deleteCurrentUploadedReport(@PathVariable BigInteger addressId, @PathVariable BigInteger textId) throws IOException {
        BigInteger userId = AuthorizedUser.id();
        Role role = userRepository.getRoleByAddress(userRepository.findOne(userId), addressRepository.findOne(addressId));

        if (Role.MANAGER.equals(role)) {
            if (reportService.findCurrentUploadedReportsOfAddress(addressId, userId, textId) == null)
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else
                reportService.deleteCurrentUploadedReport(reportService.findCurrentUploadedReportsOfAddress(addressId, userId, textId));

            FileUtils.forceDelete(FileUtils.getFile(filePath + File.separator + addressId.toString() + File.separator + textId.toString()));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}












