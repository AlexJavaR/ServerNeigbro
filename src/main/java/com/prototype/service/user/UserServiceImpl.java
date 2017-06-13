package com.prototype.service.user;

import com.prototype.model.AddressData;
import com.prototype.model.BankAccount;
import com.prototype.model.Role;
import com.prototype.repository.address.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.prototype.model.User;
import com.prototype.repository.user.UserRepository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public User save(User user) {
        //Assert.notNull(user, "user must not be null");
        if(userRepository.getByEmail(user.getEmail()) != null)
        {
            return null;
        }
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        Assert.notNull(user, "user must not be null");
        user.setUpdatedDate(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public BankAccount getBankAccount(BigInteger managerId) {
        User manager = userRepository.findOne(managerId);
        return manager.getBankAccount();
    }

    @Override
    public User findOne(BigInteger id) {
        return userRepository.findOne(id);
    }

    @Override
    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return userRepository.getByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void addDemoAddressToAllExistUsers() {
        List<User> users = userRepository.findAll();
        BigInteger addressId = new BigInteger("27611762673328170755533579207");
        for (User user : users) {
            boolean addressExist = false;
            List<AddressData> addressDataList = user.getAddressDataList();
            for (AddressData addressData : addressDataList) {
                if (addressData.getAddress().getId().equals(addressId)) {
                    addressExist = true;
                }
            }

            if (!addressExist) {
                String apart = String.valueOf(new Random().nextInt(100) + 1);
                user.getAddressDataList().add(new AddressData(addressRepository.findOne(addressId), "Demo address", apart, Role.HOUSEMATE));
            }
            userRepository.save(user);
        }
    }
}
