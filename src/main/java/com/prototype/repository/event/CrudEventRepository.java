package com.prototype.repository.event;

import com.prototype.model.event.Event;
import com.prototype.model.event.announcement.UserAnnouncementEvent;
import com.prototype.model.event.payment.BillEvent;
import com.prototype.model.event.payment.ManagerPaymentEvent;
import com.prototype.model.event.report.ReportEvent;
import com.prototype.model.event.report.UploadReportEvent;
import com.prototype.model.event.survey.SurveyEvent;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public interface CrudEventRepository extends MongoRepository<Event, BigInteger> {
    @Query(value = "{'_class' : 'com.prototype.model.event.announcement.UserAnnouncementEvent', 'address.$id' : ?0}")
    List<UserAnnouncementEvent> findAllAnnouncementsOfAddress(ObjectId objectAddressId, Sort sort);

    @Query(value = "{'address.$id' : ?0, 'apartment' : ?1, 'personal' : true, '$and' : [{'_class' : {'$ne' : 'com.prototype.model.event.payment.HousemateCashPaymentEvent'}}, {'_class' : {'$ne' : 'com.prototype.model.event.payment.HousematePayPalPaymentEvent'}}]}") //all personal event of apartment
    List<BillEvent> findAllBillsOfApartment(ObjectId objectAddressId, String apartment, Sort sort);

    @Query(value = "{'$or':[{'address.$id' : ?0, 'apartment' : ?1, 'personal' : true}, {'address.$id' : ?0, 'personal' : false, '_class' : {'$ne' : 'com.prototype.model.event.payment.ManagerPaymentEvent'}}, {'address.$id' : null}]}")
    List<Event> findGeneralEventsAsHousemate(ObjectId objectAddressId, String apartment, Sort sort);

    @Query(value = "{'$or':[{'address.$id' : ?0, 'personal' : true, '$or':[{'_class' : 'com.prototype.model.event.payment.HousemateCashPaymentEvent'}, {'_class' : 'com.prototype.model.event.payment.HousematePayPalPaymentEvent'}]}, {'address.$id' : ?0, 'personal' : false}, {'address.$id' : null}]}")
    List<Event> findGeneralEventsAsManager(ObjectId objectAddressId, Sort sort);

    @Query(value = "{'address.$id' : ?0, 'apartment' : ?1, 'personal' : true, 'settled' : false}") // only unsettled bills
    List<BillEvent> getAmountDebtOfApartment(ObjectId objectAddressId, String apartment);

    @Query(value = "{'_class' : 'com.prototype.model.event.payment.ManagerPaymentEvent', 'address.$id' : ?0}, 'dateEvent' : {$gte : ?1, $lte : ?2}}")
    List<ManagerPaymentEvent> getManagerPaymentEventBetween(ObjectId objectAddressId, LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "{'_class' : 'com.prototype.model.event.payment.ManagerPaymentEvent', 'address.$id' : ?0, 'personal' : false}")
    List<ManagerPaymentEvent> findAllManagerPaymentEvent(ObjectId objectAddressId, Sort sort);

    @Query(value = "{'$or':[{'_class' : 'com.prototype.model.event.report.GeneratedReportEvent'}, {'_class' : 'com.prototype.model.event.report.UploadReportEvent'}], 'address.$id' : ?0}")
    List<ReportEvent> findAllReportOfAddress(ObjectId objectAddressId, Sort sort);

    @Query(value = "{'_class' : 'com.prototype.model.event.report.UploadReportEvent', 'address.$id' : ?0}")
    List<UploadReportEvent> findAllUploadedReportEventOfAddress(ObjectId objectAddressId, Sort sort);

    @Query(value = "{'_class' : 'com.prototype.model.event.survey.SurveyEvent', 'address.$id' : ?0}")
    List<SurveyEvent> findAllSurveyEventByAddress(ObjectId objectAddressId, Sort dateEvent);
}
