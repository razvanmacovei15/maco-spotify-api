package com.maco.service;

import com.maco.auth.token.TokenManager;
import lombok.Getter;

@Getter
public class SpotifyService {
    private final TokenManager tokenManager;

    public SpotifyService(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }
}
