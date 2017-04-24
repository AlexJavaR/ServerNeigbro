package com.prototype.web.jwt;

import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.prototype.security.SecUserDetailsService;
import com.prototype.security.jwt.JwtAuthenticationRequest;
import com.prototype.security.jwt.JwtTokenUtil;
import com.prototype.service.jwt.JwtAuthService;
import com.prototype.service.user.UserService;
import com.prototype.to.AccessToken;
import com.prototype.model.User;
import com.prototype.to.UserWithJwt;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationRestController {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private SecUserDetailsService secUserDetailsService;

    @Autowired
    private FacebookConnectionFactory facebookConnectionFactory;

    @Autowired
    private JwtAuthService jwtAuthService;

    @Autowired
    private UserService userService;

    //String CLIENT_SECRET_FILE = "D:\\NeighBro\\server\\secret\\client_secret.json";
    private final String CLIENT_SECRET_FILE = "secret/client_secret.json";
    private final String CLIENT_ID_IOS = "194299677119-6dac4um7793t1t6t7fbrn6f3qk2g59gc.apps.googleusercontent.com";
    private final String CLIENT_ID_ANDROID = "194299677119-htja39orrub97t9ita40dsnf253nm3mh.apps.googleusercontent.com";
    private final String CLIENT_ID_WEB = "194299677119-d3onev7lrlgap9vj2fm5pbq4nbdm9ko3.apps.googleusercontent.com";

    @PostMapping(value = "${jwt.route.authentication.path}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, Device device) throws AuthenticationException {

        // Perform the security
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        // Reload password post-security so we can generate token
        final UserDetails userDetails = secUserDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails, device);
        User currentUser = userService.getByEmail(authenticationRequest.getEmail());
        UserWithJwt userWithJwt = new UserWithJwt(token, currentUser);
        //JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse(token);

        // Return the token
        return ResponseEntity.ok(userWithJwt);
    }

    @PostMapping(value = "${jwt.route.authentication.facebook.path}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAuthenticationTokenFacebook(@RequestBody AccessToken accessToken, Device device) throws AuthenticationException {
        String facebookAccessToken = accessToken.getAccessToken();
        logger.info("facebook access token from android " + facebookAccessToken);
        AccessGrant accessGrant = new AccessGrant(facebookAccessToken);
        Connection<Facebook> connection = facebookConnectionFactory.createConnection(accessGrant);
        Facebook facebook = connection.getApi();
        String[] fields = {"id", "email", "first_name", "last_name", "gender", "birthday"};
        org.springframework.social.facebook.api.User userProfile =
                facebook.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);

        String email = userProfile.getEmail();
        if (email == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User currentUser = userService.getByEmail(email);
        String userProfileBirthday = userProfile.getBirthday();
        logger.info("facebook email " + email);
        if (currentUser != null) {
            currentUser.setAvatarUrl(connection.createData().getImageUrl());
            userService.update(currentUser);
        } else{
            String facebookUserId = userProfile.getId();
            String firstName = userProfile.getFirstName();
            String lastName = userProfile.getLastName();
            String gender = userProfile.getGender();
            //CoverPhoto cover = userProfile.getCover();
            User createdUser = new User();
            createdUser.setEmail(email);
            createdUser.setFirstName(firstName);
            createdUser.setLastName(lastName);
            createdUser.setSex("male".equals(gender));
            createdUser.setAvatarUrl(connection.createData().getImageUrl());
            createdUser.setAddressDataList(new ArrayList<>());
            userService.save(createdUser);
        }

        // Reload password post-security so we can generate token
        final UserDetails userDetails = secUserDetailsService.loadUserByUsername(email);
        final String token = jwtTokenUtil.generateToken(userDetails, device);
        logger.info("facebook token from server " + token);

        UserWithJwt userWithJwt = new UserWithJwt(token, userService.getByEmail(email));

        // Return the token
        return ResponseEntity.ok(userWithJwt);
    }

    @PostMapping(value = "${jwt.route.authentication.google.path}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAuthenticationTokenGoogle(@RequestBody AccessToken accessToken, Device device) throws AuthenticationException, GeneralSecurityException, IOException {
        String googleAccessToken = accessToken.getAccessToken();
        logger.info("google access token from android " + googleAccessToken);

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new FileReader(CLIENT_SECRET_FILE));
        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(),
                        JacksonFactory.getDefaultInstance(),"https://www.googleapis.com/oauth2/v4/token",
                        clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret(), googleAccessToken, "")
                        // Specify the same redirect URI that you use with your web
                        // app. If you don't have a web version of your app, you can
                        // specify an empty string.
                        .execute();

        String accessTokenCredential = tokenResponse.getAccessToken();
        logger.info("google access ID_token " + accessTokenCredential);

        // Get profile info from ID token
        GoogleIdToken idToken = tokenResponse.parseIdToken();

        User user = jwtAuthService.getUserWithToken(idToken);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = secUserDetailsService.loadUserByUsername(user.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails, device);
        logger.info("Google token from server " + token);

        UserWithJwt userWithJwt = new UserWithJwt(token, userService.getByEmail(user.getEmail()));

        // Return the token
        return ResponseEntity.ok(userWithJwt);
    }

    @PostMapping(value = "${jwt.route.authentication.token.google.path}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAuthenticationTokenGoogleWithToken(@RequestBody AccessToken accessToken, Device device) throws AuthenticationException, GeneralSecurityException, IOException {

        final HttpTransport transport = new NetHttpTransport();
        final JsonFactory jsonFactory = new JacksonFactory();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Arrays.asList(CLIENT_ID_IOS, CLIENT_ID_ANDROID, CLIENT_ID_WEB))
                .setIssuer("https://accounts.google.com")
                .build();

        GoogleIdToken idToken = verifier.verify(accessToken.getAccessToken());
        if (idToken != null) {
            User user = jwtAuthService.getUserWithToken(idToken);

            // Reload password post-security so we can generate token
            final UserDetails userDetails = secUserDetailsService.loadUserByUsername(user.getEmail());
            final String token = jwtTokenUtil.generateToken(userDetails, device);

            UserWithJwt userWithJwt = new UserWithJwt(token, userService.getByEmail(user.getEmail()));

            // Return the token
            return ResponseEntity.ok(userWithJwt);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping(value = "${jwt.route.authentication.refresh}")
//    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
//        String token = request.getHeader(tokenHeader);
//        String username = jwtTokenUtil.getUsernameFromToken(token);
//        AuthorizedUser user = (AuthorizedUser) userDetailsService.loadUserByUsername(username);
//
//        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
//            String refreshedToken = jwtTokenUtil.refreshToken(token);
//            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
//        } else {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
}
