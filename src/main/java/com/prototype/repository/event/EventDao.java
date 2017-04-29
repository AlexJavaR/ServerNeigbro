package com.prototype.repository.event;

import com.prototype.model.event.Event;
import com.prototype.model.event.payment.ManagerPaymentEvent;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class EventDao {

    @Autowired
    MongoOperations mongo;

    public List<ManagerPaymentEvent> getManagerPaymentEventBetween(ObjectId objectAddressId, LocalDateTime startDate, LocalDateTime endDate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_class").is("com.prototype.model.event.payment.ManagerPaymentEvent"))
                .addCriteria(Criteria.where("address.$id").is(objectAddressId))
                .addCriteria(Criteria.where("dateEvent").gte(startDate).lt(endDate)).with(new Sort(Sort.Direction.DESC, "dateEvent"));

        return mongo.find(query, ManagerPaymentEvent.class);
    }

    List<Event> findGeneralEventsOfAddress(ObjectId objectAddressId, String apartment) {
        Criteria criteria1 = new Criteria().andOperator(Criteria.where("address.$id").is(objectAddressId),
                Criteria.where("apartment").is(apartment), Criteria.where("personal").is(true));
        Criteria criteria2 = new Criteria().andOperator(Criteria.where("address.$id").is(objectAddressId),
                Criteria.where("apartment").is(null), Criteria.where("personal").is(false));
        Criteria criteria3 = Criteria.where("address.$id").is(null);
        Criteria criteria = new Criteria().orOperator(criteria1, criteria2, criteria3);
        Query query = new Query();
        query.addCriteria(criteria).with(new Sort(Sort.Direction.DESC, "dateEvent"));
        return mongo.find(query, Event.class);
    }
}
