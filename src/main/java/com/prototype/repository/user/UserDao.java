package com.prototype.repository.user;

import com.prototype.model.AddressData;
import com.prototype.model.Role;
import com.prototype.model.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Component
public class UserDao {

    @Autowired MongoOperations mongo;

    public List<AddressData> getOneTenantsAddressDataPerEachApartment(ObjectId addressId) {

        Aggregation agg = newAggregation(
                match(Criteria.where("addressDataList.address.$id").is(addressId)),
                unwind("addressDataList"),
                match(Criteria.where("addressDataList.address.$id").is(addressId)
                        .and("addressDataList.role").is(Role.HOUSEMATE)),
                /*project() // http://stackoverflow.com/questions/19434237/can-i-use-project-to-return-a-field-as-the-top-level-document-in-a-mongo-aggreg
                        .andExpression("addressDataList.apartment").as("apartment")
                        .andExpression("addressDataList.role").as("role")
                        .andExpression("addressDataList.address").as("address"),*/
                group("$addressDataList.apartment")
                    .first("$addressDataList.role").as("role")
                    .first("$addressDataList.apartment").as("apartment")
                    .first("$addressDataList.address").as("address")
                /*
                 { "$group" : { "_id" : "$addressDataList.apartment",
                    "role": { $first: "$addressDataList.role" },
                    "address" : {$first: "$addressDataList.address"}}}
                 */
        );

        return mongo.aggregate(agg, User.class, AddressData.class).getMappedResults();
    }

}
