package com.prototype.service.user;

import com.prototype.model.User;

import java.math.BigInteger;
import java.util.List;

public interface UserService {

    User save(User user);

    User findOne(BigInteger id);

    User getByEmail(String email);

    List<User> findAll();

    User update(User user);
}
