package com.maco.spotify.auth.token;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
public class SpotifyToken {
    private final String accessToken;
    private final String refreshToken;
    private final long expiresIn;
    private final Instant createdAt;
    private final String tokenType;

    public SpotifyToken(String accessToken, String refreshToken, long expiresIn, String tokenType) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.createdAt = Instant.now();
        this.tokenType = tokenType;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(createdAt.plusSeconds(expiresIn));
    }

    public String getAuthorizationHeader() {
        return tokenType + " " + accessToken;
    }
}
