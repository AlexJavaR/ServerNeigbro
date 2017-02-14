package com.prototype.service.user;

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
    private UserRepository repository;

    @Override
    public User save(User user) {
        //Assert.notNull(user, "user must not be null");
        if(repository.getByEmail(user.getEmail()) != null)
        {
            return null;
        }
        return repository.save(user);
    }

    @Override
    public User update(User user) {
        Assert.notNull(user, "user must not be null");
        return repository.save(user);
    }

    @Override
    public User findOne(BigInteger id) {
        return repository.findOne(id);
    }

    @Override
    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return repository.getByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }
}
