package com.prototype.service.jwt;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.prototype.model.User;

public interface JwtAuthService {
    User getUserWithToken(GoogleIdToken idToken);
}
