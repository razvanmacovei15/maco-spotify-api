package com.maco.spotify.auth.token;

import lombok.Getter;

import java.time.Instant;

@Getter
public class SpotifyToken {
    private final String accessToken;
    private final String refreshToken;
    private final long expiresIn;
    private final Instant createdAt;
    private final String tokenType;
    @Getter
    private final String scope;

    public SpotifyToken(String accessToken, String refreshToken, long expiresIn, String tokenType, String scope) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.createdAt = Instant.now();
        this.tokenType = tokenType;
        this.scope = scope;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(createdAt.plusSeconds(expiresIn));
    }

    public String getAuthorizationHeader() {
        return tokenType + " " + accessToken;
    }

    @Override
    public String toString() {
        return "SpotifyToken{" +
                "expiresIn=" + expiresIn +
                ", createdAt=" + createdAt +
                ", tokenType='" + tokenType + '\'' +
                ", scope='" + scope + '\'' +
                ", isExpired=" + isExpired() +
                '}';
    }
}
