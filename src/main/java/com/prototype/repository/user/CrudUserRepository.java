package com.prototype.repository.user;

import com.prototype.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface CrudUserRepository extends MongoRepository<User, BigInteger> {

    User getByEmail(String email);

    //(value = "{ 'userId' : ?0, 'questions.questionID' : ?1 }", fields = "{ 'questions.questionID' : 1 }")

    @Query(value = "{ 'addressDataList.address.$id' : ?0 }")
    List<User> getOneUserPerApartment(ObjectId addressId);
}
