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
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Service("ReportService")
public class ReportServiceImpl implements ReportService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EventDao eventDao;

    @Override
    public GeneratedReportEvent createReport(BigInteger userId, BigInteger addressId, LocalDateTime startDate, LocalDateTime endDate) {
        Address currentAddress = addressRepository.findOne(addressId);
        ObjectId objectAddressId = new ObjectId(addressId.toString(16));
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
    public UploadReportEvent createEmptyUploadedReportEvent( UploadReportEvent uploadReportEvent)
    {
        return eventRepository.save(uploadReportEvent);
    }

    @Override
    public UploadReportEvent createUploadReportEvent(BigInteger userId, BigInteger addressId, File file) {
        if (userId != null && addressId != null && file.length() != 0) {
            Address address = addressRepository.findOne(addressId);
            User user = userRepository.findOne(userId);
            Role role = userRepository.getRoleByAddress(user, address);
            if (user != null && address != null) {
                if (Role.MANAGER.equals(role)) {
                    UploadReportEvent uploadReportEvent = new UploadReportEvent(LocalDateTime.now(), address, file.getName(), file.length());


                    return eventRepository.save(uploadReportEvent);
                }
            }
        }
        return null;
    }
//
//
//    public UploadReportEvent createUploadReportEvent2(BigInteger userId, BigInteger addressId, UploadReportEvent uploadReportEvent, File file) {
//        if (userId != null && addressId != null && file.length() != 0) {
//            Address address = addressRepository.findOne(addressId);
//            User user = userRepository.findOne(userId);
//            Role role = userRepository.getRoleByAddress(user, address);
//            if (user != null && address != null) {
//                if (Role.MANAGER.equals(role)) {
//
//                    ObjectId objectId =  new ObjectId(uploadReportEvent.getId().toString(16));
//                    return eventRepository.setFileNameUploadedReportEvent(o, file.getName());
//                }
//            }
//
//        }
//         return null;
//    }

    @Override
    public List<UploadReportEvent> findAllUploadedReportsOfAddress(BigInteger addressId, BigInteger userId) {
        ObjectId objectAddressId = new ObjectId(addressId.toString(16));
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
        ObjectId objectAddressId = new ObjectId(addressId.toString(16));
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
    public UploadReportEvent findCurrentUploadedReportsOfAddress(BigInteger addressId, BigInteger userId, BigInteger textId) {
        User user = userRepository.findOne(userId);
        ObjectId objectAddressId = new ObjectId((addressId.toString(16)));
        for (AddressData addressData : user.getAddressDataList()) {
            if (Objects.equals(addressData.getAddress().getId(), addressId)) {
                List<UploadReportEvent> uploadReportEvents = eventRepository.findAllUploadedReportEventOfAddress(objectAddressId);
                for (UploadReportEvent u : uploadReportEvents)
                    if (u.getId().equals(textId))
                        return u;
            }
        }
        return null;
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
    public void deleteCurrentUploadedReport(UploadReportEvent uploadReportEvent) {
        eventRepository.delete(uploadReportEvent.getId());
    }
}
