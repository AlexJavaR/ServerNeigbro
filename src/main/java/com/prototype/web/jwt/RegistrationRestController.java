package com.prototype.web.jwt;

import com.prototype.model.AddressData;
import com.prototype.model.User;
import com.prototype.security.SecUserDetailsService;
import com.prototype.security.jwt.JwtTokenUtil;
import com.prototype.service.address.AddressService;
import com.prototype.service.user.UserService;
import com.prototype.to.UserWithJwt;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = RegistrationRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RegistrationRestController {

    static final String REST_URL = "/api/v1";

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private SecUserDetailsService secUserDetailsService;

    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody User user, Device device) {
        String email = user.getEmail();
        boolean valid = EmailValidator.getInstance().isValid(email);
        if (!valid) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        AddressData addressData = addressService.getDemoAddressData();
        List<AddressData> addressDataList = new ArrayList<>();
        addressDataList.add(addressData);
        user.setAddressDataList(addressDataList);
        User createdUser = userService.save(user);
        if (createdUser == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        final UserDetails userDetails = secUserDetailsService.loadUserByUsername(email);
        final String token = jwtTokenUtil.generateToken(userDetails, device);

        UserWithJwt userWithJwt = new UserWithJwt(token, createdUser);

        // Return the token
        return new ResponseEntity<>(userWithJwt, HttpStatus.CREATED);
    }

    @GetMapping(value = "/add/demo/address")
    public ResponseEntity<Boolean> addDemoAddress() {
        userService.addDemoAddressToAllExistUsers();
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
