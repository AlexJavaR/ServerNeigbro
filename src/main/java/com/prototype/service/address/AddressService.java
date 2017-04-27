package com.prototype.service.address;

import com.prototype.model.Address;
import com.prototype.model.AddressData;

import java.math.BigInteger;
import java.util.List;

public interface AddressService {
//    User saveHMAddress(BigInteger addressId, BigInteger userId, String apartment);
//    User saveNewVBAddress(Address address, BigInteger userId);
    List<AddressData> findAllAddressData(BigInteger userId);

    List<Address> findAllAddress();

    Address update(Address address, BigInteger userId);

    Address findOne(BigInteger addressId);

    Address addAddressAsManager(AddressData addressData, BigInteger userId);

    Address addAddressAsHousemate(AddressData addressData, BigInteger userId);

    Address findAddressByPlaceIdAndEntrance(String placeId, String entrance);

    AddressData updateAddressData(AddressData addressData, BigInteger userId);
}
