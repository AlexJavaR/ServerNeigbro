package com.prototype.repository.event;

import com.prototype.model.event.ApartmentEvent;
import com.prototype.model.event.Event;
import com.prototype.model.event.announcement.UserAnnouncementEvent;
import com.prototype.model.event.payment.BillEvent;
import com.prototype.model.event.payment.ManagerPaymentEvent;
import com.prototype.model.event.report.ReportEvent;
import com.prototype.model.event.report.UploadReportEvent;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class EventRepositoryImpl implements EventRepository {

    @Autowired
    private CrudEventRepository crudEventRepository;

    @Override
    public <S extends Event> S save(S entity) {
        return crudEventRepository.save(entity);
    }

    @Override
    public Event findOne(BigInteger id) {
        return crudEventRepository.findOne(id);
    }

    @Override
    public void delete(BigInteger id) {
        crudEventRepository.delete(id);
    }


    @Override
    public List<UserAnnouncementEvent> findAllAnnouncementsOfAddress(ObjectId objectAddressId) {
        return crudEventRepository.findAllAnnouncementsOfAddress(objectAddressId);
    }

    @Override
    public List<ApartmentEvent> findAllPersonalEventOfApartment(ObjectId objectAddressId, String apartment) {
        return crudEventRepository.findAllPersonalEventOfApartment(objectAddressId, apartment);
    }

    @Override
    public List<Event> findGeneralEventsOfAddress(ObjectId objectAddressId, String apartment) {
        return crudEventRepository.findGeneralEventsOfAddress(objectAddressId, apartment);
    }

    @Override
    public List<BillEvent> getAmountDebtOfApartment(ObjectId objectAddressId, String apartment) {
        return crudEventRepository.getAmountDebtOfApartment(objectAddressId, apartment);
    }

    @Override
    public List<ManagerPaymentEvent> getManagerPaymentEventBetween(ObjectId objectAddressId, LocalDateTime startDate, LocalDateTime endDate) {
        return crudEventRepository.getManagerPaymentEventBetween(objectAddressId, startDate, endDate);
    }

    @Override
    public List<ManagerPaymentEvent> findAllManagerPaymentEvent(ObjectId objectAddressId) {
        return crudEventRepository.findAllManagerPaymentEvent(objectAddressId);
    }

    @Override
    public List<ReportEvent> findAllReportOfAddress(ObjectId objectAddressId) {
        return crudEventRepository.findAllReportOfAddress(objectAddressId);
    }

    @Override
    public List<UploadReportEvent> findAllUploadedReportEventOfAddress(ObjectId objectAddressId) {
        return crudEventRepository.findAllUploadedReportEventOfAddress(objectAddressId);
    }
}
