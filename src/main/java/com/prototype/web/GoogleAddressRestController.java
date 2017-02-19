package com.prototype.web;

import com.prototype.model.Address;
import com.prototype.model.AddressData;
import com.prototype.model.Role;
import com.prototype.security.AuthorizedUser;
import com.prototype.service.address.AddressService;
import com.prototype.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = GoogleAddressRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class GoogleAddressRestController {
    static final String REST_URL = "/api/v1/google";

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    //for add address as manager or housemate
    @PostMapping(value = "/address", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Address> addAddress(@RequestBody AddressData addressData) {
        //TODO validate new address
        BigInteger userId = AuthorizedUser.id();
        if (addressData.getAddress() == null || addressData.getAddress().getGoogleAddress() == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        List<AddressData> list = userService.findOne(userId).getAddressDataList()
                .stream().filter(ad -> Objects.equals(ad.getAddress().getGoogleAddress().getPlaceId(), addressData.getAddress().getGoogleAddress().getPlaceId()))
                .collect(Collectors.toList());
        Address address = null;
        if(!list.isEmpty()) {
            if (Role.MANAGER.equals(addressData.getRole()) || !list.stream().filter(ad -> ad.getRole().equals(Role.MANAGER)).collect(Collectors.toList()).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if (Role.MANAGER.equals(addressData.getRole())) {
            if (addressData.getAddress().getFirstApartment() == null
                    || addressData.getAddress().getLastApartment() == null
                    || addressData.getAddress().getPhoneNumber() == null
                    || addressData.getAddress().getMonthlyFee() == null) {
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            }
            address = addressService.addAddressAsManager(addressData, userId);
        }

        if (Role.HOUSEMATE.equals(addressData.getRole())) {
            if (addressData.getApartment() == null || "".equals(addressData.getApartment())) {
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            }
            address = addressService.addAddressAsHousemate(addressData, userId);
        }
        if (address == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(address, HttpStatus.CREATED);
    }

    //edit address by manager
    @PutMapping(value = "/address", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressData> editAddressByManager(@RequestBody AddressData addressData) {
        BigInteger userId = AuthorizedUser.id();
        if (addressData.getAddress() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Address currentAddress = addressService.findOne(addressData.getAddress().getId());
        if (currentAddress == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AddressData updateAddressData = addressService.updateAddressData(addressData, userId);
        if (updateAddressData == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(updateAddressData, HttpStatus.OK);
    }

    //for add address as manager or housemate
    @GetMapping(value = "/address/{placeId}")
    public ResponseEntity<Boolean> isManagerExist(@PathVariable("placeId") String placeId) {
        //TODO validate new address
        BigInteger userId = AuthorizedUser.id();
        Address address = addressService.findAddressByPlaceId(placeId);
        if (address == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
        boolean isManagerExist = address.isManagerExist();
        return new ResponseEntity<>(isManagerExist, HttpStatus.OK);
    }

    @GetMapping(value = "/address")
    public ResponseEntity<List<AddressData>> findAllGoogleAddressUser() {
        BigInteger userId = AuthorizedUser.id();
        List<AddressData> addressData = addressService.findAll(userId);
        return new ResponseEntity<>(addressData, HttpStatus.OK);
    }

    @GetMapping(value = "/me/address/{addressId}")
    public ResponseEntity<Address> findGoogleAddressUser(@PathVariable("addressId") BigInteger addressId) {
        Address address = addressService.findOne(addressId);
        if (address == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(address, HttpStatus.OK);
    }
}
