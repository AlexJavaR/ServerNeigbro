package com.prototype.web.address;

import com.prototype.model.Address;
import com.prototype.model.AddressData;
import com.prototype.model.User;
import com.prototype.repository.address.AddressRepository;
import com.prototype.service.address.AddressService;
import com.prototype.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.*;

@RestController
@RequestMapping(value = AdminGoogleAddressController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminGoogleAddressController {
    static final String REST_URL = "/api/v1/google";

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressRepository addressRepository;


    @GetMapping(value = "/map/city")
    public ResponseEntity<Map<String, Integer>> findAllUserByCity() {
        List<User> userList = userService.findAll();
        List<AddressData> addressDataList = new ArrayList<>();
        for (User u : userList) {
            addressDataList.addAll(u.getAddressDataList());
        }
        Map<String, Integer> cities = new HashMap<>();
        for (AddressData addressData : addressDataList) {
            String city = addressData.getAddress().getGoogleAddress().getCity();
            if (cities.containsKey(city)){
                cities.put(city, cities.get(city) + 1);
            } else {
                cities.put(city, 1);
            }
        }
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    @GetMapping(value = "/address/zero")
    public ResponseEntity<List<Address>> setZeroAmountUserInHouse() {
        List<Address> addressList = addressService.findAllAddress();
        for (Address address : addressList) {
            address.getGoogleAddress().setAmountUser(0);
            addressRepository.save(address);
        }
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    @GetMapping(value = "/address/total")
    public ResponseEntity<Set<Address>> getAllGoogleAddressWithTotalUsers() {
        List<User> userList = userService.findAll();
        Set<Address> addressSet = new HashSet<>();
        List<AddressData> addressDataList = new ArrayList<>();
        for (User u : userList) {
            addressDataList.addAll(u.getAddressDataList());
        }

        for (AddressData addressData : addressDataList) {
            BigInteger addressId = addressData.getAddress().getId();
            Address address = addressRepository.findOne(addressId);
            address.getGoogleAddress().setAmountUser(address.getGoogleAddress().getAmountUser() + 1);
            addressSet.add(address);
            addressRepository.save(address);
        }
        return new ResponseEntity<>(addressSet, HttpStatus.OK);
    }
}
