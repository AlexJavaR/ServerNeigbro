package com.prototype.repository.address;

import com.prototype.model.Address;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigInteger;

public interface CrudAddressRepository extends MongoRepository<Address, BigInteger> {
    @Query(value = "{'googleAddress.placeId' : ?0}")
    Address findByPlaceId(String placeId);
}
