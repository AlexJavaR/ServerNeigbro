package com.prototype.repository.event;

import com.prototype.model.event.payment.ManagerPaymentEvent;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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
                .addCriteria(Criteria.where("dateEvent").gte(startDate).lt(endDate));

        return mongo.find(query, ManagerPaymentEvent.class);
    }
}
