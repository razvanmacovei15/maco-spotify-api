package com.maco.spotify.auth.token;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maco.spotify.api.config.SpotifyConfig;
import com.maco.spotify.auth.http.SpotifyHttpClient;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Slf4j
public class TokenManager {
    @Setter
    private SpotifyToken currentToken;
    private final SpotifyConfig config;
    private final SpotifyHttpClient httpClient;

    public TokenManager(SpotifyConfig config) {
        this.config = config;
        this.httpClient = new SpotifyHttpClient();
        log.info("TokenManager initialized with config: {}", config);
    }

    public void authenticate(String code) {
        try {
            log.info("Starting authentication with code");
            Map<String, String> headers = createAuthHeaders();
            Map<String, String> formData = Map.of(
                    "grant_type", "authorization_code",
                    "code", code,
                    "redirect_uri", config.getRedirectUri()
            );

            log.info("Sending token request to Spotify");
            TokenResponse response = httpClient.post(
                    "https://accounts.spotify.com/api/token",
                    headers,
                    formData,
                    TokenResponse.class
            );
            log.info("Received token response: {}", response);

            SpotifyToken newToken = new SpotifyToken(
                    response.accessToken,
                    response.refreshToken,
                    response.expiresIn,
                    response.tokenType,
                    response.scope
            );
            setCurrentToken(newToken);
            log.info("Token set successfully: {}", newToken);
        } catch (IOException e) {
            log.error("Failed to authenticate with Spotify", e);
            throw new RuntimeException("Failed to authenticate with Spotify", e);
        }
    }

    public SpotifyToken getCurrentToken() {
        if (currentToken == null) {
            log.warn("No token available. Please authenticate first.");
            throw new IllegalStateException("No token available. Please authenticate first.");
        }

        if (currentToken.isExpired()) {
            log.info("Token expired, refreshing...");
            refreshToken();
        }

        log.debug("Returning current token: {}", currentToken);
        return currentToken;
    }

    public void refreshToken() {
        if (currentToken == null || currentToken.getRefreshToken() == null) {
            log.error("No refresh token available. Please re-authenticate.");
            throw new IllegalStateException("No refresh token available. Please re-authenticate.");
        }

        try {
            log.info("Starting token refresh");
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
            log.info("Received refresh response: {}", response);

            SpotifyToken newToken = new SpotifyToken(
                    response.accessToken,
                    response.refreshToken,
                    response.expiresIn,
                    response.tokenType,
                    response.scope
            );
            setCurrentToken(newToken);
            log.info("Token refreshed successfully: {}", newToken);
        } catch (IOException e) {
            log.error("Failed to refresh token", e);
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

    public void revokeToken() throws IOException {
        if (currentToken == null) {
            return; // Nothing to revoke
        }

        Map<String, String> headers = createAuthHeaders();
        Map<String, String> formData = Map.of(
            "token", currentToken.getAccessToken()
        );

        httpClient.post(
            "https://accounts.spotify.com/api/token/revoke",
            headers,
            formData,
            String.class
        );

        // Clear the current token
        currentToken = null;
    }

    public boolean isTokenExpired() {
        if (currentToken == null) {
            return true;
        }
        return currentToken.isExpired();
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

        @Override
        public String toString() {
            return "TokenResponse{" +
                    "accessToken='" + accessToken + '\'' +
                    ", refreshToken='" + refreshToken + '\'' +
                    ", expiresIn=" + expiresIn +
                    ", tokenType='" + tokenType + '\'' +
                    ", scope='" + scope + '\'' +
                    '}';
        }
    }

}
