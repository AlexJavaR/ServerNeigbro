package com.prototype.repository.address;

import com.prototype.model.Address;
import com.prototype.model.AddressData;
import com.prototype.model.Role;
import com.prototype.model.User;
import com.prototype.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Repository
public class AddressRepositoryImpl implements AddressRepository {

    @Autowired
    private CrudAddressRepository crudAddressRepository;

    @Override
    public Address update(Address address) {
        return crudAddressRepository.save(address);
    }

    @Override
    public Address save(Address address) {
        return crudAddressRepository.save(address);
    }

    @Override
    public Address findOne(BigInteger addressId) {
        return crudAddressRepository.findOne(addressId);
    }

    @Override
    public List<Address> findAll() {
        return crudAddressRepository.findAll();
    }

    @Override
    public Address findByPlaceId(String placeId) {
        return crudAddressRepository.findByPlaceId(placeId);
    }
}
