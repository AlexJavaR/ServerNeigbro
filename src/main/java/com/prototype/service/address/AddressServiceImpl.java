package com.prototype.service.address;

import com.prototype.repository.address.AddressRepository;
import com.prototype.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.prototype.model.Address;
import com.prototype.model.AddressData;
import com.prototype.model.Role;
import com.prototype.model.User;


@Service("AddressService")
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

//    @Override
//    public User saveNewVBAddress(Address address, BigInteger userId) {
//        for (int i = address.getFirstApartment(); i <= address.getLastApartment(); i++) {
//            address.getListOfApartment().add(String.valueOf(i));
//        }
//
//        Address savedAddress = addressRepository.save(address);
//        User user = userRepository.findOne(userId);
//        AddressData addressData = new AddressData(address, null, null, Role.MANAGER);
//        if (!user.getAddressDataList().isEmpty()) {
//            for (AddressData list : user.getAddressDataList()) {
//                if (list.equals(addressData)) return null;
//            }
//        }
//        user.getAddressDataList().add(new AddressData(savedAddress, null, null, Role.MANAGER));
//        return userRepository.save(user);
//    }
//
//    @Override
//    public User saveHMAddress(BigInteger addressId, BigInteger userId, String apartment) {
//        Address address = addressRepository.findOne(addressId);
//        User user = userRepository.findOne(userId);
//        AddressData addressData = new AddressData(address, null, apartment, Role.HOUSEMATE);
//        if (!user.getAddressDataList().isEmpty()) {
//            for (AddressData list : user.getAddressDataList()) {
//                if (addressData.equals(list)) return null;
//            }
//        }
//        user.getAddressDataList().add(addressData);
//        return userRepository.save(user);
//    }

    @Override
    public List<AddressData> findAll(BigInteger userId) {
        User currentUser = userRepository.findOne(userId);
        return currentUser.getAddressDataList();
    }

    @Override
    public AddressData updateAddressData(AddressData addressData, BigInteger userId) {
        Address currentAddress = addressRepository.findOne(addressData.getAddress().getId());
        User currentUser = userRepository.findOne(userId);
        AddressData currentAddressData = userRepository.getAddressDataByAddress(currentUser, currentAddress);
        if (addressData.getRole().equals(Role.MANAGER)) {
            currentAddressData.setTitle(addressData.getTitle() != null ? addressData.getTitle() : currentAddressData.getTitle());
            currentAddress.setEntrance(addressData.getAddress().getEntrance() != null ? addressData.getAddress().getEntrance() : currentAddress.getEntrance());
            currentAddress.setFirstApartment(addressData.getAddress().getFirstApartment() != null ? addressData.getAddress().getFirstApartment() : currentAddress.getFirstApartment());
            currentAddress.setLastApartment(addressData.getAddress().getLastApartment() != null ? addressData.getAddress().getLastApartment() : currentAddress.getLastApartment());
            currentAddress.setMonthlyFee(addressData.getAddress().getMonthlyFee() != null ? addressData.getAddress().getMonthlyFee() : currentAddress.getMonthlyFee());
            currentAddress.setPhoneNumber(addressData.getAddress().getPhoneNumber() != null ? addressData.getAddress().getPhoneNumber() : currentAddress.getPhoneNumber());
            currentAddressData.setAddress(currentAddress);
            if (addressData.getAddress().getFirstApartment() != null && addressData.getAddress().getLastApartment() != null) {
                currentAddress.setListOfApartment(new ArrayList<>());
                for (int i = addressData.getAddress().getFirstApartment(); i <= addressData.getAddress().getLastApartment(); i++) {
                    currentAddress.getListOfApartment().add(String.valueOf(i));
                }
            }
            userRepository.save(currentUser);
            addressRepository.update(currentAddress);
            return currentAddressData;
        } else if (addressData.getRole().equals(Role.HOUSEMATE)) {
            if (currentAddress.isManagerExist() && currentAddress.getListOfApartment().contains(addressData.getApartment())) {
                currentAddressData.setTitle(addressData.getTitle() != null ? addressData.getTitle() : currentAddressData.getTitle());
                currentAddressData.setApartment(addressData.getApartment() != null ? addressData.getApartment() : currentAddressData.getApartment());
            } else if (!currentAddress.isManagerExist()) {
                currentAddressData.setTitle(addressData.getTitle() != null ? addressData.getTitle() : currentAddressData.getTitle());
                currentAddressData.setApartment(addressData.getApartment() != null ? addressData.getApartment() : currentAddressData.getApartment());
            } else return null;
            userRepository.save(currentUser);
            return currentAddressData;
        }
        return null;
    }

    @Override
    public Address update(Address address, BigInteger userId) {
        Address currentAddress = addressRepository.findOne(address.getId());
        User currentUser = userRepository.findOne(userId);
        AddressData addressData = userRepository.getAddressDataByAddress(currentUser, address);
        if (userRepository.getRoleByAddress(currentUser, currentAddress).equals(Role.MANAGER)) {
            addressData.setTitle(address.getTitle() != null ? address.getTitle() : addressData.getTitle());
            currentAddress.setEntrance(address.getEntrance() != null ? address.getEntrance() : currentAddress.getEntrance());
            currentAddress.setFirstApartment(address.getFirstApartment() != null ? address.getFirstApartment() : currentAddress.getFirstApartment());
            currentAddress.setLastApartment(address.getLastApartment() != null ? address.getLastApartment() : currentAddress.getLastApartment());
            currentAddress.setMonthlyFee(address.getMonthlyFee() != null ? address.getMonthlyFee() : currentAddress.getMonthlyFee());
            currentAddress.setPhoneNumber(address.getPhoneNumber() != null ? address.getPhoneNumber() : currentAddress.getPhoneNumber());
            if (address.getFirstApartment() != null || address.getLastApartment() != null) {
                currentAddress.setListOfApartment(new ArrayList<>());
                for (int i = address.getFirstApartment(); i <= address.getLastApartment(); i++) {
                    currentAddress.getListOfApartment().add(String.valueOf(i));
                }
            }
            userRepository.save(currentUser);
            return addressRepository.update(currentAddress);
        } else return null;
    }

    @Override
    public Address findOne(BigInteger addressId) {
        return addressRepository.findOne(addressId);
    }

    @Override
    public Address addAddressAsManager(AddressData addressData, BigInteger userId) {
        User currentUser = userRepository.findOne(userId);
        String placeId = addressData.getAddress().getGoogleAddress().getPlaceId();
        Address address = addressRepository.findByPlaceId(placeId);
        if (Role.MANAGER.equals(addressData.getRole())) {
            if (address == null) {
                address = new Address();
                address.setGoogleAddress(addressData.getAddress().getGoogleAddress());
                address.setEntrance(addressData.getAddress().getEntrance());
            }
            address.setAmountForWithdrawal(0);
            address.setListOfApartment(new ArrayList<>());
            address.setFirstApartment(addressData.getAddress().getFirstApartment());
            address.setLastApartment(addressData.getAddress().getLastApartment());
            for (int i = address.getFirstApartment(); i <= address.getLastApartment(); i++) {
                address.getListOfApartment().add(String.valueOf(i));
            }
            address.setMonthlyFee(addressData.getAddress().getMonthlyFee());
            address.setPhoneNumber(addressData.getAddress().getPhoneNumber());
            address.setAccountBalance(0);
            address.setFundAddress(0);
            address.setManagerExist(true);
        }
        address = addressRepository.save(address);
        //AddressData addressData = new AddressData(address, jsonGoogleAddress.getTitle(), null, Role.MANAGER);
        addressData.setAddress(address);
        if (!currentUser.getAddressDataList().isEmpty()) {
            for (AddressData list : currentUser.getAddressDataList()) {
                if (list.equals(addressData)) return null;
            }
        }
        currentUser.getAddressDataList().add(addressData);
        userRepository.save(currentUser);
        return address;
    }

    @Override
    public Address addAddressAsHousemate(AddressData addressData, BigInteger userId) {
        User currentUser = userRepository.findOne(userId);
        String placeId = addressData.getAddress().getGoogleAddress().getPlaceId();
        Address address = addressRepository.findByPlaceId(placeId);
        if (address == null) {
            address = new Address();
            address.setGoogleAddress(addressData.getAddress().getGoogleAddress());
            address.setEntrance(addressData.getAddress().getEntrance());
            address = addressRepository.save(address);
        } else {
            if (address.getListOfApartment() != null && !address.getListOfApartment().isEmpty()) {
                if (!address.getListOfApartment().contains(addressData.getApartment())) {
                    return null;
                }
            }
        }
        AddressData newAddressData = new AddressData(address, addressData.getTitle(), addressData.getApartment(), Role.HOUSEMATE);
        if (!currentUser.getAddressDataList().isEmpty()) {
            for (AddressData list : currentUser.getAddressDataList()) {
                if (list.equals(newAddressData)) return null;
            }
        }
        currentUser.getAddressDataList().add(newAddressData);
        userRepository.save(currentUser);
        return address;
    }

    @Override
    public Address findAddressByPlaceId(String placeId) {
        return addressRepository.findByPlaceId(placeId);
    }
}
