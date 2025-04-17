package com.maco.auth.token;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maco.auth.config.SpotifyConfig;
import com.maco.auth.http.MyHttpClient;
import lombok.Setter;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class TokenManager {
    @Setter
    private AuthToken currentToken;
    private final SpotifyConfig config;
    private final MyHttpClient httpClient;

    public TokenManager(SpotifyConfig config) {
        this.config = config;
        this.httpClient = new MyHttpClient();
    }

    public void authenticate(String code) {
        try {
            Map<String, String> headers = createAuthHeaders();
            Map<String, String> formData = Map.of(
                    "grant_type", "authorization_code",
                    "code", code,
                    "redirect_uri", config.getRedirectUri()
            );

            TokenResponse response = httpClient.post(
                    "https://accounts.spotify.com/api/token",
                    headers,
                    formData,
                    TokenResponse.class
            );

            setCurrentToken(new AuthToken(
                    response.accessToken,
                    response.refreshToken,
                    response.expiresIn,
                    response.tokenType
            ));
        } catch (IOException e) {
            throw new RuntimeException("Failed to authenticate with Spotify", e);
        }
    }

    public AuthToken getCurrentToken() {
        if (currentToken == null) {
            throw new IllegalStateException("No token available. Please authenticate first.");
        }

        if (currentToken.isExpired()) {
            refreshToken();
        }

        return currentToken;
    }

    private void refreshToken() {
        if (currentToken == null || currentToken.getRefreshToken() == null) {
            throw new IllegalStateException("No refresh token available. Please re-authenticate.");
        }

        try {
            Map<String, String> headers = createAuthHeaders();
            Map<String, String> formData = Map.of(
                    "grant_type", "refresh_token",
                    "refresh_token", currentToken.getRefreshToken()
            );

            TokenResponse response = httpClient.post(
                    "https://accounts.spotify.com/api/token",
                    headers,
                    formData,
                    TokenResponse.class
            );

            setCurrentToken(new AuthToken(
                    response.accessToken,
                    response.refreshToken,
                    response.expiresIn,
                    response.tokenType
            ));
        } catch (IOException e) {
            throw new RuntimeException("Failed to refresh token", e);
        }
    }

    private Map<String, String> createAuthHeaders() {
        return Map.of(
                "Authorization", "Basic " + Base64.getEncoder().encodeToString(
                        (config.getClientId() + ":" + config.getClientSecret()).getBytes()
                )
        );
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class TokenResponse {
        @JsonProperty
        public String accessToken;
        @JsonProperty
        public String refreshToken;
        @JsonProperty
        public long expiresIn;
        @JsonProperty
        public String tokenType;
        @JsonProperty("scope")
        private String scope;
    }
}
