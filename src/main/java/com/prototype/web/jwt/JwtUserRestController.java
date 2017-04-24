package com.prototype.web.jwt;

import com.prototype.security.AuthorizedUser;
import com.prototype.security.SecUserDetailsService;
import com.prototype.security.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class JwtUserRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private SecUserDetailsService secUserDetailsService;

    @GetMapping(value = "user")
    public AuthorizedUser getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        AuthorizedUser user = (AuthorizedUser) secUserDetailsService.loadUserByUsername(username);
        return user;
    }

}
