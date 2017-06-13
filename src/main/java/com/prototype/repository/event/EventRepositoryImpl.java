package com.prototype.repository.event;

import com.prototype.model.event.Event;
import com.prototype.model.event.announcement.UserAnnouncementEvent;
import com.prototype.model.event.payment.BillEvent;
import com.prototype.model.event.payment.ManagerPaymentEvent;
import com.prototype.model.event.report.ReportEvent;
import com.prototype.model.event.report.UploadReportEvent;
import com.prototype.model.event.survey.SurveyEvent;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EventRepositoryImpl implements EventRepository {

    @Autowired
    private CrudEventRepository crudEventRepository;

    @Override
    public <S extends Event> S save(S entity) {
        Event saveEvent = crudEventRepository.save(entity);
        return (S) findOne(saveEvent.getId());
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
        return crudEventRepository.findAllAnnouncementsOfAddress(objectAddressId, new Sort(Sort.Direction.DESC, "dateEvent"));
    }

    @Override
    public List<BillEvent> findAllBillsOfApartment(ObjectId objectAddressId, String apartment) {
        return crudEventRepository.findAllBillsOfApartment(objectAddressId, apartment, new Sort(Sort.Direction.DESC, "dateEvent"));
    }

    @Override
    public List<Event> findGeneralEventsAsHousemate(ObjectId objectAddressId, String apartment) {
        //List<Event> generalEventsOfAddress = crudEventRepository.findGeneralEventsOfAddress(objectAddressId, apartment);
        //Collections.sort(generalEventsOfAddress, (o1, o2) -> o2.getDateEvent().compareTo(o1.getDateEvent()));
        return crudEventRepository.findGeneralEventsAsHousemate(objectAddressId, apartment, new Sort(Sort.Direction.DESC, "dateEvent"));
        //return eventDao.findGeneralEventsOfAddress(objectAddressId, apartment);
    }

    @Override
    public List<Event> findGeneralEventsAsManager(ObjectId objectAddressId) {
        //List<Event> generalEventsOfAddress = crudEventRepository.findGeneralEventsOfAddress(objectAddressId, apartment);
        //Collections.sort(generalEventsOfAddress, (o1, o2) -> o2.getDateEvent().compareTo(o1.getDateEvent()));
        return crudEventRepository.findGeneralEventsAsManager(objectAddressId, new Sort(Sort.Direction.DESC, "dateEvent"));
        //return eventDao.findGeneralEventsOfAddress(objectAddressId, apartment);
    }

    @Override
    public List<BigInteger> getIdAllUnsettledBillsOfApartment(ObjectId objectAddressId, String apartment) {
        List<BillEvent> listUnsettledBillEvents = crudEventRepository.getAmountDebtOfApartment(objectAddressId, apartment);
        List<BigInteger> listUnsettledBillEventsId = new ArrayList<>();
        listUnsettledBillEvents.forEach(b -> listUnsettledBillEventsId.add(b.getId()));
        return listUnsettledBillEventsId;
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
        return crudEventRepository.findAllManagerPaymentEvent(objectAddressId, new Sort(Sort.Direction.DESC, "dateEvent"));
    }

    @Override
    public List<ReportEvent> findAllReportOfAddress(ObjectId objectAddressId) {
        return crudEventRepository.findAllReportOfAddress(objectAddressId, new Sort(Sort.Direction.DESC, "dateEvent"));
    }

    @Override
    public List<UploadReportEvent> findAllUploadedReportEventOfAddress(ObjectId objectAddressId) {
        return crudEventRepository.findAllUploadedReportEventOfAddress(objectAddressId, new Sort(Sort.Direction.DESC, "dateEvent"));
    }

    @Override
    public List<SurveyEvent> findAllSurveyEventByAddress(ObjectId objectAddressId) {
        return crudEventRepository.findAllSurveyEventByAddress(objectAddressId, new Sort(Sort.Direction.DESC, "dateEvent"));
    }
}
