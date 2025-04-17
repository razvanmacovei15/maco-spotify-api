package com.maco.auth;

import com.maco.auth.model.AuthToken;

public interface Authenticator {
    String createAuthorizationUrl(String... scopes);
    AuthToken authenticate(String code);
    AuthToken refreshToken(String refreshToken);
}
