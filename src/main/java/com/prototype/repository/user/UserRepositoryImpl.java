package com.prototype.repository.user;


import com.prototype.model.Address;
import com.prototype.model.AddressData;
import com.prototype.model.Role;
import com.prototype.model.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    CrudUserRepository crudRepository;

    @Override
    public User getByEmail(String email) {
        return crudRepository.getByEmail(email);
    }


    @Override
    public User findOne(BigInteger userId) {
        return crudRepository.findOne(userId);
    }

    @Override
    public User save(User user) {
        return crudRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return crudRepository.findAll();
    }

    @Override
    public Role getRoleByAddress(User user, Address address) {
        for (AddressData ad : user.getAddressDataList()) {
            if (ad.getAddress().equals(address)) {
                return ad.getRole();
            }
        }
        return null;
    }

    @Override
    public AddressData getAddressDataByAddress(User user, Address address) {
        for (AddressData addressData : user.getAddressDataList()) {
            if (Objects.equals(addressData.getAddress().getId(), address.getId())) return addressData;
        }
        return null;
    }

    @Override
    public List<User> getOneUserPerApartment(ObjectId addressId) {
        List<User> users = crudRepository.getOneUserPerApartment(addressId);
        for (User user : users) {
            System.out.println(user.getEmail());
        }
        return users;
        //return null;
    }
}
