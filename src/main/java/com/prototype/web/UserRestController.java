package com.prototype.web;

import com.prototype.security.AuthorizedUser;
import com.prototype.model.User;
import com.prototype.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping(value = UserRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {

    static final String REST_URL = "/api/v1";

    @Autowired
    private UserService userService;

    //-------------------Retrieve All Users-------------------------------------------
    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //-------------------Retrieve Single User-----------------------------------------

    @GetMapping(value = "/me")
    public ResponseEntity<User> findOne() {
        BigInteger id = AuthorizedUser.id();
        User user = userService.findOne(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //------------------- Update a User --------------------------------------------------------

    @PutMapping(value = "/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        BigInteger userId = AuthorizedUser.id();

        User currentUser = userService.findOne(userId);

        if (currentUser == null) {
            System.out.println("User with id " + userId + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        currentUser.setFirstName(user.getFirstName() != null ? user.getFirstName() : currentUser.getFirstName());
        currentUser.setLastName(user.getLastName() != null ? user.getLastName() : currentUser.getLastName());
        currentUser.setBirthday(user.getBirthday() != null ? user.getBirthday() : currentUser.getBirthday());
        currentUser.setSex(user.isSex());

        currentUser = userService.update(currentUser);
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @GetMapping(value = "/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpSession session) {
        session.invalidate();
    }
}