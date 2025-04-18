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
    }

    public void authenticate(String code) {
        try {
            log.info("Authenticating with Spotify using code: {}", code != null ? "[REDACTED]" : "null");

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

            this.currentToken = new SpotifyToken(
                    response.accessToken,
                    response.refreshToken,
                    response.expiresIn,
                    response.tokenType
            );

            log.info("Authentication successful. Token acquired and stored.");
        } catch (IOException e) {
            log.error("Failed to authenticate with Spotify", e);
            throw new RuntimeException("Failed to authenticate with Spotify", e);
        }
    }

    public SpotifyToken getCurrentToken() {
        if (currentToken == null) {
            throw new IllegalStateException("No token available. Please authenticate first.");
        }

        if (currentToken.isExpired()) {
            log.info("Token expired. Refreshing...");
            refreshToken();
        }

        return currentToken;
    }

    public void refreshToken() {
        if (currentToken == null || currentToken.getRefreshToken() == null) {
            throw new IllegalStateException("No refresh token available. Please re-authenticate.");
        }

        try {
            log.info("Refreshing Spotify access token...");

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

            this.currentToken = new SpotifyToken(
                    response.accessToken,
                    response.refreshToken,
                    response.expiresIn,
                    response.tokenType
            );

            log.info("Token refreshed successfully.");
        } catch (IOException e) {
            log.error("Failed to refresh Spotify token", e);
            throw new RuntimeException("Failed to refresh Spotify token", e);
        }
    }

    private Map<String, String> createAuthHeaders() {
        String authValue = Base64.getEncoder().encodeToString(
                (config.getClientId() + ":" + config.getClientSecret()).getBytes()
        );

        return Map.of(
                "Authorization", "Basic " + authValue
        );
    }

    public void revokeToken() throws IOException {
        if (currentToken == null) {
            log.warn("Attempted to revoke token, but no token is currently set.");
            return;
        }

        log.info("Revoking current Spotify access token...");

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

        currentToken = null;
        log.info("Token revoked and cleared from memory.");
    }

    public boolean isTokenExpired() {
        return currentToken == null || currentToken.isExpired();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class TokenResponse {
        @JsonProperty("access_token")
        public String accessToken;

        @JsonProperty("refresh_token")
        public String refreshToken;

        @JsonProperty("expires_in")
        public long expiresIn;

        @JsonProperty("token_type")
        public String tokenType;

        @JsonProperty("scope")
        public String scope;
    }
}
