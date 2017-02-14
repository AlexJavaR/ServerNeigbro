package com.prototype.repository.address;

import com.prototype.model.Address;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

public interface AddressRepository {
    Address update(Address address);

    Address save(Address address);

    Address findOne(BigInteger addressId);

    List<Address> findAll();

    Address findByPlaceId(String placeId);
}
