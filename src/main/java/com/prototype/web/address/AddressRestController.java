package com.prototype.web.address;

import com.prototype.model.AddressData;
import com.prototype.security.AuthorizedUser;
import com.prototype.model.Address;
import com.prototype.service.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping(value = AddressRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AddressRestController {

    static final String REST_URL = "/api/v1";

    @Autowired
    private AddressService addressService;

    @GetMapping(value = "/me/address")
    public ResponseEntity<List<AddressData>> findAllAddressUser() {
        BigInteger userId = AuthorizedUser.id();
        List<AddressData> addressData = addressService.findAllAddressData(userId);
        return new ResponseEntity<>(addressData, HttpStatus.OK);
    }

    @GetMapping(value = "/me/address/{addressId}")
    public ResponseEntity<Address> findAddressUser(@PathVariable("addressId") BigInteger addressId) {
        if (addressId == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Address address = addressService.findOne(addressId);
        if (address == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(address, HttpStatus.OK);
    }


    //for add housemate by invitation
//    @PostMapping(value = "/me/address", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Void> addHousemateToAddress(@RequestBody HousemateAddressData housemateAddressData) {
//        //TODO validate exist address
//        BigInteger userId = AuthorizedUser.id();
//        String addressStringId = housemateAddressData.getAddressId();
//        BigInteger addressId;
//        if (AuthorizedUser.get() == null) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//        try {
//            addressId = new BigInteger(addressStringId);
//        } catch (NumberFormatException e) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//        Address currentAddress = addressService.findOne(addressId);
//        List<AddressData> list = userService.findOne(userId).getAddressDataList()
//                .stream().filter(ad -> Objects.equals(ad.getAddress().getId(), addressId))
//                .collect(Collectors.toList());
//        if (!list.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//        if (currentAddress == null || !currentAddress.getListOfApartment().contains(housemateAddressData.getApartment())) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        User currentUser = addressService.saveHMAddress(addressId, userId, housemateAddressData.getApartment());
//
//        if (housemateAddressData.getApartment() == null || "".equals(housemateAddressData.getApartment()) || currentUser == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
//        }
//        if (addressService.findOne(addressId) == null) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }

    //edit address by manager
    @PutMapping(value = "/me/address", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Address> editAddress(@RequestBody Address address) {
        BigInteger userId = AuthorizedUser.id();
        Address currentAddress = addressService.findOne(address.getId());
        if (currentAddress == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Address updateAddress = addressService.update(address, userId);
        if (updateAddress == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(updateAddress, HttpStatus.OK);
    }
}
