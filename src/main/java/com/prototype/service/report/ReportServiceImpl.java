package com.prototype.service.report;

import com.prototype.model.Address;
import com.prototype.model.AddressData;
import com.prototype.model.Role;
import com.prototype.model.User;
import com.prototype.model.event.payment.ManagerPaymentEvent;
import com.prototype.model.event.report.GeneratedReportEvent;
import com.prototype.model.event.report.ReportEvent;
import com.prototype.model.event.report.UploadReportEvent;
import com.prototype.repository.address.AddressRepository;
import com.prototype.repository.event.EventDao;
import com.prototype.repository.event.EventRepository;
import com.prototype.repository.user.UserRepository;
import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Service("ReportService")
public class ReportServiceImpl implements ReportService {

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final AddressRepository addressRepository;

    private final EventDao eventDao;

    private String filePath = "uploadedReports";

    @Autowired
    public ReportServiceImpl(UserRepository userRepository, EventRepository eventRepository, AddressRepository addressRepository, EventDao eventDao) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.addressRepository = addressRepository;
        this.eventDao = eventDao;
    }

    @Override
    public GeneratedReportEvent createReport(BigInteger userId, BigInteger addressId, LocalDateTime startDate, LocalDateTime endDate) {
        Address currentAddress = addressRepository.findOne(addressId);
        ObjectId objectAddressId = convertBigIntegerToObjectId(addressId);
        if (objectAddressId == null) {
            return null;
        }
        GeneratedReportEvent generatedReportEvent = new GeneratedReportEvent(endDate, currentAddress);

        //List<ManagerPaymentEvent> listManagerPayments = eventRepository.getManagerPaymentEventBetween(objectAddressId, startDate, endDate);
        List<ManagerPaymentEvent> listManagerPayments = eventDao.getManagerPaymentEventBetween(objectAddressId, startDate, endDate);
        generatedReportEvent.setListManagerPayments(listManagerPayments);

        if (userId == null && !listManagerPayments.isEmpty()) {
            return eventRepository.save(generatedReportEvent);
        } else if (!listManagerPayments.isEmpty()) {
            return generatedReportEvent;
        } else return null;
    }

    @Override
    public UploadReportEvent createEmptyUploadedReportEvent(UploadReportEvent uploadReportEvent) {
        return eventRepository.save(uploadReportEvent);
    }

    @Override
    public UploadReportEvent uploadFileForUploadReportEvent(BigInteger userId, BigInteger addressId, MultipartFile uploadedFile) {
        User manager = userRepository.findOne(userId);
        Address address = addressRepository.findOne(addressId);
        if (Role.MANAGER.equals(userRepository.getRoleByAddress(manager, address))) {
            try {
                byte[] bytes = uploadedFile.getBytes();
                File dir = new File(filePath + File.separator + addressId.toString());
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String fileName = uploadedFile.getOriginalFilename();
                String[] fileArraySplit = fileName.split("\\.");
                String fileExtension = fileArraySplit[fileArraySplit.length - 1];
                UploadReportEvent uploadReportEvent =
                        new UploadReportEvent(LocalDateTime.now(), address, fileName, fileExtension, uploadedFile.getSize());
                createEmptyUploadedReportEvent(uploadReportEvent);
                String name = uploadReportEvent.getId().toString();

                File file = new File(dir.getAbsolutePath() + File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
                stream.write(bytes);
                stream.flush();
                stream.close();
                return uploadReportEvent;
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void deleteCurrentUploadedReportAndFile(BigInteger userId, BigInteger addressId, BigInteger reportId) {
        User manager = userRepository.findOne(userId);
        Address address = addressRepository.findOne(addressId);

        if (Role.MANAGER.equals(userRepository.getRoleByAddress(manager, address))) {
            if (findCurrentUploadedReportsOfAddress(addressId, userId, reportId) != null) {
                eventRepository.delete(reportId);

                try {
                    FileUtils.forceDelete(FileUtils.getFile(filePath + File.separator + addressId.toString() + File.separator + reportId.toString()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<UploadReportEvent> findAllUploadedReportsOfAddress(BigInteger addressId, BigInteger userId) {
        ObjectId objectAddressId = convertBigIntegerToObjectId(addressId);
        if (objectAddressId == null) {
            return null;
        }
        User user = userRepository.findOne(userId);
        for (AddressData addressData : user.getAddressDataList()) {
            if (Objects.equals(addressData.getAddress().getId(), addressId)) {
                return eventRepository.findAllUploadedReportEventOfAddress(objectAddressId);
            }
        }
        return null;
    }


    @Override
    public List<ReportEvent> findAllReportOfAddress(BigInteger addressId, BigInteger userId) {
        ObjectId objectAddressId = convertBigIntegerToObjectId(addressId);
        if (objectAddressId == null) {
            return null;
        }
        User currentUser = userRepository.findOne(userId);
        for (AddressData addressData : currentUser.getAddressDataList()) {
            if (Objects.equals(addressData.getAddress().getId(), addressId)) {
                return eventRepository.findAllReportOfAddress(objectAddressId);
            }
        }
        return null;
    }

    @Override
    public GeneratedReportEvent createCurrentReport(BigInteger userId, BigInteger addressId) {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startDate = currentDate.with(firstDayOfMonth()).with(LocalTime.MIDNIGHT);

        User currentUser = userRepository.findOne(userId);
        for (AddressData addressData : currentUser.getAddressDataList()) {
            if (Objects.equals(addressData.getAddress().getId(), addressId)) {
                return createReport(userId, addressId, startDate, currentDate);
            }
        }
        return null;
    }

    @Override
    public File findCurrentUploadedReportsOfAddress(BigInteger addressId, BigInteger userId, BigInteger reportId) {
        User user = userRepository.findOne(userId);
        Address address = addressRepository.findOne(addressId);
        ObjectId objectAddressId = convertBigIntegerToObjectId(addressId);
        if (objectAddressId == null) {
            return null;
        }
        AddressData addressData = userRepository.getAddressDataByAddress(user, address);
        if (Objects.equals(addressData.getAddress().getId(), addressId)) {
            UploadReportEvent uploadReportEvent = (UploadReportEvent) eventRepository.findOne(reportId);
            if (uploadReportEvent == null) {
                return null;
            }
        }

        File file = FileUtils.getFile(filePath + File.separator + addressId.toString() + File.separator + reportId.toString());
        return file;
    }

    //@Scheduled(fixedRate = 500)
    @Scheduled(cron = "0 3 0 1 * ?")
    @Override
    public void createMonthlyReport() {
        LocalDateTime currentDate = LocalDateTime.now().minusMonths(1);
        LocalDateTime startDate = currentDate.with(firstDayOfMonth()).with(LocalTime.MIDNIGHT);
        LocalDateTime endDate = currentDate.with(lastDayOfMonth()).with(LocalTime.MAX);
        addressRepository.findAll().forEach(address -> createReport(null, address.getId(), startDate, endDate));
    }

    @Override
    public void createTestMonthlyReport() {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime startDate = currentDate.with(firstDayOfMonth()).with(LocalTime.MIDNIGHT);
        LocalDateTime endDate = LocalDateTime.now();
        addressRepository.findAll().forEach(address -> createReport(null, address.getId(), startDate, endDate));
    }

    public ObjectId convertBigIntegerToObjectId(BigInteger addressId) {
        ObjectId objectAddressId;
        try {
            objectAddressId = new ObjectId(addressId.toString(16));
        } catch (IllegalArgumentException e) {
            return null;
        }
        return objectAddressId;
    }
}
