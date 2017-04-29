package com.prototype.web.address;

import com.prototype.model.Address;
import com.prototype.model.AddressData;
import com.prototype.model.Role;
import com.prototype.security.AuthorizedUser;
import com.prototype.service.address.AddressService;
import com.prototype.service.user.UserService;
import com.prototype.to.CityWithAllHouses;
import com.prototype.to.HouseLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.*;
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
                .stream().filter(ad -> ad.getAddress().equals(addressData.getAddress()))
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
    public ResponseEntity<AddressData> editAddress(@RequestBody AddressData addressData) {
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
    @GetMapping(value = "/address/verify")
    public ResponseEntity<Boolean> isManagerExist(@RequestParam(value = "placeId", required = false) String placeId,
                                                  @RequestParam(value = "entrance", required = false) String entrance) {
        //TODO validate new address
        if (placeId == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        BigInteger userId = AuthorizedUser.id();
        Address address = addressService.findAddressByPlaceIdAndEntrance(placeId, entrance);
        if (address == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
        boolean isManagerExist = address.isManagerExist();
        return new ResponseEntity<>(isManagerExist, HttpStatus.OK);
    }

    //for add address as manager or housemate
    @GetMapping(value = "/verify")
    public ResponseEntity<Address> isAddressExist(@RequestParam(value = "placeId", required = false) String placeId,
                                                  @RequestParam(value = "entrance", required = false) String entrance) {
        if (placeId == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Address address = addressService.findAddressByPlaceIdAndEntrance(placeId, entrance);
        if (address == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @GetMapping(value = "/address")
    public ResponseEntity<List<AddressData>> findAllGoogleAddressUser() {
        BigInteger userId = AuthorizedUser.id();
        List<AddressData> addressData = addressService.findAllAddressData(userId);
        return new ResponseEntity<>(addressData, HttpStatus.OK);
    }

    @GetMapping(value = "/me/address/{addressId}")
    public ResponseEntity<Address> findGoogleAddressUser(@PathVariable("addressId") BigInteger addressId) {
        if (addressId == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Address address = addressService.findOne(addressId);
        if (address == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @GetMapping(value = "/map/city/location")
    public ResponseEntity<Map<String, CityWithAllHouses>> findAllCityWithLocation() {
        List<Address> addressList = addressService.findAllAddress();
        Map<String, CityWithAllHouses> cities = new HashMap<>();
        for (Address address : addressList) {
            String city = address.getGoogleAddress().getCity() == null ? "City" : address.getGoogleAddress().getCity();
            if (cities.containsKey(city)){
                CityWithAllHouses cityWithAllHouses = cities.get(city);
                cityWithAllHouses.setTotal(cityWithAllHouses.getTotal() + address.getGoogleAddress().getAmountUser());
                cityWithAllHouses.getHouses().add(new HouseLocation(address.getGoogleAddress().getAmountUser(),
                        address.getGoogleAddress().getLatitude(), address.getGoogleAddress().getLongitude()));
                cities.put(city, cityWithAllHouses);
            } else {
                CityWithAllHouses cityWithAllHouses = new CityWithAllHouses();
                cityWithAllHouses.setTotal(address.getGoogleAddress().getAmountUser());
                cityWithAllHouses.getHouses().add(new HouseLocation(address.getGoogleAddress().getAmountUser(),
                        address.getGoogleAddress().getLatitude(), address.getGoogleAddress().getLongitude()));
                cities.put(city, cityWithAllHouses);
            }
        }
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }
}
