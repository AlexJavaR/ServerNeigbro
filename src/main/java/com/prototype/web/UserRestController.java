package com.prototype.web;

import com.prototype.security.AuthorizedUser;
import com.prototype.model.User;
import com.prototype.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.apache.commons.validator.routines.EmailValidator;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = UserRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {

    static final String REST_URL = "/api/v1";

    @Autowired
    private UserService userService;

    //-------------------Retrieve All Users-------------------------------------------
    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> findAll()
    {
        List<User> users = userService.findAll();
        if(users.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //-------------------Retrieve Single User-----------------------------------------

    @GetMapping(value = "/me")
    public ResponseEntity<User> findOne()
    {
        BigInteger id = AuthorizedUser.id();
        User user = userService.findOne(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //-------------------Create a User-------------------------------------------------

    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        String email = user.getEmail();
        boolean valid = EmailValidator.getInstance().isValid(email);
        if (!valid)
        {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        user.setAddressDataList(new ArrayList<>());
        User createdUser = userService.save(user);
        if (createdUser == null)
        {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

//        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .buildAndExpand().toUri();
//        return ResponseEntity.created(uriOfNewResource).body(user);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    //------------------- Update a User --------------------------------------------------------

    @PutMapping(value = "/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        BigInteger userId = AuthorizedUser.id();

        User currentUser = userService.findOne(userId);

        if (currentUser==null) {
            System.out.println("User with id " + userId + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        currentUser.setFirstName(user.getFirstName() != null ? user.getFirstName() : currentUser.getFirstName());
        currentUser.setLastName(user.getLastName() != null ? user.getLastName() : currentUser.getLastName());
        currentUser.setBirthday(user.getBirthday() != null ? user.getBirthday() : currentUser.getBirthday());
        currentUser.setSex(user.isSex());
        currentUser.setUpdatedDate(LocalDateTime.now());

        currentUser = userService.update(currentUser);
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }
}