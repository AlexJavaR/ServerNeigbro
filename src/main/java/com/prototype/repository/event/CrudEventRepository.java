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

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public interface CrudEventRepository extends MongoRepository<Event, BigInteger> {
    @Query(value = "{'_class' : 'com.prototype.model.event.announcement.UserAnnouncementEvent', 'address.$id' : ?0}")
    List<UserAnnouncementEvent> findAllAnnouncementsOfAddress(ObjectId objectAddressId);

    @Query(value = "{'address.$id' : ?0, 'apartment' : ?1, 'personal' : true}") //all personal event of apartment
    List<ApartmentEvent> findAllPersonalEventOfApartment(ObjectId objectAddressId, String apartment);

    @Query(value = "{'$or':[{'address.$id' : ?0, 'apartment' : ?1, 'personal' : true}, {'address.$id' : ?0, 'apartment' : null, 'personal' : false}, {'address.$id' : null}]}")
        //@Query(value = "{'address.$id' : ?0, 'apartment' : ?1, 'personal' : true}")
    List<Event> findGeneralEventsOfAddress(ObjectId objectAddressId, String apartment);

    @Query(value = "{'address.$id' : ?0, 'apartment' : ?1, 'personal' : true, 'settled' : false}") // only unsettled bills
    List<BillEvent> getAmountDebtOfApartment(ObjectId objectAddressId, String apartment);

    @Query(value = "{'_class' : 'com.prototype.model.event.payment.ManagerPaymentEvent', 'address.$id' : ?0}, 'dateEvent' : {$gte : ?1, $lte : ?2}}")
    List<ManagerPaymentEvent> getManagerPaymentEventBetween(ObjectId objectAddressId, LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "{'_class' : 'com.prototype.model.event.payment.ManagerPaymentEvent', 'address.$id' : ?0, 'personal' : false}")
    List<ManagerPaymentEvent> findAllManagerPaymentEvent(ObjectId objectAddressId);

    //@Query(value = "{'_class' : 'com.prototype.model.event.report.GeneratedReportEvent', 'address.$id' : ?0}")
    @Query(value = "{'$or':[{'_class' : 'com.prototype.model.event.report.GeneratedReportEvent'}, {'_class' : 'com.prototype.model.event.report.UploadReportEvent'}], 'address.$id' : ?0}")
    List<ReportEvent> findAllReportOfAddress(ObjectId objectAddressId);

    @Query(value = "{'_class' : 'com.prototype.model.event.report.UploadReportEvent', 'address.$id' : ?0}")
    List<UploadReportEvent> findAllUploadedReportEventOfAddress(ObjectId objectAddressId);
}
