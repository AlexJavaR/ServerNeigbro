package com.prototype.repository.user;

import com.prototype.model.Address;
import com.prototype.model.AddressData;
import com.prototype.model.Role;
import com.prototype.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

public interface UserRepository {
    User getByEmail(String email);

    List<User> getOneUserPerApartment(ObjectId addressId);

    User findOne(BigInteger userId);

    User save(User user);

    List<User> findAll();

    Role getRoleByAddress(User user, Address address);

    AddressData getAddressDataByAddress(User user, Address address);

    AddressData getAddressData(User user, Address address, String apartment);
}
