package com.prototype.service.address;

import com.prototype.model.Address;
import com.prototype.model.AddressData;
import com.prototype.model.User;
import com.prototype.to.HousemateAddressData;
import com.prototype.to.JsonGoogleAddress;

import java.math.BigInteger;
import java.util.List;

public interface AddressService {
//    User saveHMAddress(BigInteger addressId, BigInteger userId, String apartment);
//    User saveNewVBAddress(Address address, BigInteger userId);
    List<AddressData> findAll(BigInteger userId);

    Address update(Address address, BigInteger userId);

    Address findOne(BigInteger addressId);

    Address addAddressAsManager(AddressData addressData, BigInteger userId);

    Address addAddressAsHousemate(AddressData addressData, BigInteger userId);

    Address findAddressByPlaceId(String placeId);
}
