package com.prototype.repository.event;

import com.prototype.model.event.ApartmentEvent;
import com.prototype.model.event.Event;
import com.prototype.model.event.announcement.UserAnnouncementEvent;
import com.prototype.model.event.payment.BillEvent;
import com.prototype.model.event.payment.ManagerPaymentEvent;
import com.prototype.model.event.report.ReportEvent;
import com.prototype.model.event.report.UploadReportEvent;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository {

    <S extends Event> S save(S entity);

    Event findOne(BigInteger id);

    void delete(BigInteger id);

    List<UserAnnouncementEvent> findAllAnnouncementsOfAddress(ObjectId objectAddressId);

    List<ApartmentEvent> findAllPersonalEventOfApartment(ObjectId objectAddressId, String apartment);

    List<Event> findGeneralEventsOfAddress(ObjectId objectAddressId, String apartment);

    List<BillEvent> getAmountDebtOfApartment(ObjectId objectAddressId, String apartment);

    List<ManagerPaymentEvent> getManagerPaymentEventBetween(ObjectId objectAddressId, LocalDateTime startDate, LocalDateTime endDate);

    List<ManagerPaymentEvent> findAllManagerPaymentEvent(ObjectId objectAddressId);

    List<ReportEvent> findAllReportOfAddress(ObjectId objectAddressId);

    List<UploadReportEvent> findAllUploadedReportEventOfAddress(ObjectId objectAddressId);
}
