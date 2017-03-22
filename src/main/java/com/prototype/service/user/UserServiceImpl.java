package com.prototype.service.user;

import com.prototype.model.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.prototype.model.User;
import com.prototype.repository.user.UserRepository;

import java.math.BigInteger;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
}
