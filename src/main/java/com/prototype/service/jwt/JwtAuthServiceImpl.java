package com.prototype.service.jwt;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.prototype.model.User;
import com.prototype.repository.user.UserRepository;
import com.prototype.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("JwtAuthService")
public class JwtAuthServiceImpl implements JwtAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public User getUserWithToken(GoogleIdToken idToken) {
        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();

        User currentUser = userRepository.getByEmail(email);
        if (currentUser != null) {
            currentUser.setAvatarUrl((String) payload.get("picture"));
            currentUser = userService.update(currentUser);
        } else {
            //String userId = payload.getSubject();  // Use this value as a key to identify a user.
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            String avatarUrl = (String) payload.get("picture");
            User createdUser = new User();
            createdUser.setEmail(email);
            createdUser.setFirstName(firstName);
            createdUser.setLastName(lastName);
            createdUser.setAvatarUrl(avatarUrl);
            createdUser.setAddressDataList(new ArrayList<>());
            currentUser = userRepository.save(createdUser);
        }

        return currentUser;
    }
}
