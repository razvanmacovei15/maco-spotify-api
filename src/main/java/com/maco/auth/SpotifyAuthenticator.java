package com.maco.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maco.auth.config.ClientConfig;
import com.maco.auth.http.MyHttpClient;
import com.maco.auth.model.AuthToken;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class SpotifyAuthenticator implements Authenticator{
    private final ClientConfig clientConfig;
    private final MyHttpClient httpClient;

    public SpotifyAuthenticator(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        this.httpClient = new MyHttpClient();
    }

    @Override
    public String createAuthorizationUrl(String... scopes) {
        String scopeString = String.join(" ", scopes);
        return "https://accounts.spotify.com/authorize" +
                "?client_id=" + clientConfig.getClientId() +
                "&response_type=code" +
                "&redirect_uri=" + clientConfig.getRedirectUri() +
                "&scope=" + scopeString;
    }

    @Override
    public AuthToken authenticate(String code) {
        try {
            Map<String, String> headers = Map.of(
                "Authorization", "Basic " + Base64.getEncoder().encodeToString(
                    (clientConfig.getClientId() + ":" + clientConfig.getClientSecret()).getBytes()
                )
            );

            Map<String, String> formData = Map.of(
                "grant_type", "authorization_code",
                "code", code,
                "redirect_uri", clientConfig.getRedirectUri()
            );

            TokenResponse response = httpClient.post(
                "https://accounts.spotify.com/api/token",
                headers,
                formData,
                TokenResponse.class
            );

            return new AuthToken(
                response.accessToken,
                response.refreshToken,
                response.expiresIn,
                response.tokenType
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to authenticate with Spotify", e);
        }
    }

    @Override
    public AuthToken refreshToken(String refreshToken) {
        try {
            Map<String, String> headers = Map.of(
                "Authorization", "Basic " + Base64.getEncoder().encodeToString(
                    (clientConfig.getClientId() + ":" + clientConfig.getClientSecret()).getBytes()
                )
            );

            Map<String, String> formData = Map.of(
                "grant_type", "refresh_token",
                "refresh_token", refreshToken
            );

            TokenResponse response = httpClient.post(
                "https://accounts.spotify.com/api/token",
                headers,
                formData,
                TokenResponse.class
            );

            return new AuthToken(
                response.accessToken,
                response.refreshToken,
                response.expiresIn,
                response.tokenType
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to refresh token with Spotify", e);
        }
    }

    private static class TokenResponse{
        @JsonProperty
        public String accessToken;
        @JsonProperty
        public String refreshToken;
        @JsonProperty
        public long expiresIn;
        @JsonProperty
        public String tokenType;
    }
}
