package com.maco.client.v2;

import com.maco.client.v2.enums.TimeRange;
import com.maco.client.v2.interfaces.SpotifyClient;
import com.maco.client.v2.interfaces.TokenUpdateListener;
import com.maco.client.v2.model.SpotifyArtist;
import com.maco.client.v2.model.SpotifyTrack;
import com.maco.client.v2.model.SpotifyUser;
import com.maco.client.v2.model.response.TokenResponse;
import com.maco.client.v2.service.SpotifyArtistsService;
import com.maco.client.v2.service.SpotifyTracksService;
import com.maco.client.v2.service.SpotifyUserService;
import com.maco.client.v2.utils.SpotifyHttpClient;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
@Data
@AllArgsConstructor
public class SpotifyClientI implements SpotifyClient {
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String[] scopes;

    private SpotifyToken token;
    private boolean isAuthenticated;
    private SpotifyHttpClient spotifyHttpClient;

    private final SpotifyArtistsService spotifyArtistsService;
    private final SpotifyTracksService spotifyTracksService;
    private final SpotifyUserService spotifyUserService;

    @Setter
    private TokenUpdateListener tokenUpdateListener;


    public SpotifyClientI(String clientId, String clientSecret, String redirectUri, String[] scopes) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scopes = scopes;
        this.token = null;
        this.isAuthenticated = false;
        this.spotifyHttpClient = new SpotifyHttpClient();
        this.spotifyTracksService = new SpotifyTracksService(spotifyHttpClient ,clientId, clientSecret, null);
        this.spotifyArtistsService = new SpotifyArtistsService(spotifyHttpClient, clientId, clientSecret, null);
        this.spotifyUserService = new SpotifyUserService(spotifyHttpClient, clientId, clientSecret, null);
    }


    public boolean isExpired() {
        return Instant.now().isAfter(token.getCreatedAt().plusSeconds(token.getExpiresIn()));
    }

    private Map<String, String> createAuthHeaders() {
        return Map.of(
                "Authorization", "Basic " + Base64.getEncoder().encodeToString(
                        (clientId + ":" + clientSecret).getBytes()
                )
        );
    }

    private Map<String, String> createHeaders() {
        return Map.of(
                "Authorization", token.getAuthorizationHeader()
        );
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public String getAuthorizationUrl(String state) {
        try {
            String encodedScopes = URLEncoder.encode(String.join(" ", scopes), StandardCharsets.UTF_8);
            String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
            String encodedState = URLEncoder.encode(state, StandardCharsets.UTF_8);

            return "https://accounts.spotify.com/authorize" +
                    "?client_id=" + clientId +
                    "&response_type=code" +
                    "&redirect_uri=" + encodedRedirectUri +
                    "&scope=" + encodedScopes +
                    "&state=" + encodedState;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create authorization URL", e);
        }
    }

    private void setHeaders(){
        spotifyArtistsService.setHeaders(createHeaders());
        spotifyTracksService.setHeaders(createHeaders());
        spotifyUserService.setHeaders(createHeaders());
    }

    @Override
    public void authenticate(String code) {
        try {
            log.info("Starting authentication with code");
            Map<String, String> headers = createAuthHeaders();
            Map<String, String> formData = Map.of(
                    "grant_type", "authorization_code",
                    "code", code,
                    "redirect_uri", redirectUri
            );

            log.info("Sending token request to Spotify");
            TokenResponse response = spotifyHttpClient.post(
                    "https://accounts.spotify.com/api/token",
                    headers,
                    formData,
                    TokenResponse.class
            );
            log.info("Received token response: {}", response);

            if (response == null || response.getAccessToken() == null) {
                log.error("Invalid token response received from Spotify");
                throw new RuntimeException("Invalid token response received from Spotify");
            }

            SpotifyToken newToken = new SpotifyToken(
                    response.getAccessToken(),
                    response.getRefreshToken(),
                    response.getTokenType(),
                    response.getExpiresIn(),
                    response.getScope(),
                    Instant.now()
            );
            this.token = newToken;
            this.isAuthenticated = true;
            setHeaders();
            log.info("Authentication successful. Token set: {}", newToken);
        } catch (IOException e) {
            log.error("Failed to authenticate with Spotify", e);
            this.isAuthenticated = false;
            throw new RuntimeException("Failed to authenticate with Spotify", e);
        }
    }

    @Override
    public void deAuthenticate() {
        try {
            if (token == null) {
                return; // Nothing to revoke
            }

            Map<String, String> headers = createAuthHeaders();
            Map<String, String> formData = Map.of(
                    "token", token.getAccessToken()
            );

            spotifyHttpClient.post(
                    "https://accounts.spotify.com/api/token/revoke",
                    headers,
                    formData,
                    String.class
            );

            // Clear the current token
            token = null;
        } catch (IOException e) {
            log.error("Failed to revoke token", e);
            throw new RuntimeException("Failed to revoke token", e);
        }
    }

    @Override
    public SpotifyToken getAccessToken() {
        return token;
    }

    @Override
    public void setAccessToken(String accessToken, String refreshToken, String tokenType, int expiresIn, String scope) {
        if (accessToken == null || accessToken.isEmpty()) {
            log.error("Access token cannot be null or empty");
            throw new IllegalArgumentException("Access token cannot be null or empty");
        }
        SpotifyToken newToken = new SpotifyToken(
                accessToken,
                refreshToken,
                tokenType,
                expiresIn,
                scope,
                Instant.now()
        );
        this.token = newToken;
        this.isAuthenticated = true;
        setHeaders();
        log.info("Token set successfully: {}", newToken);
    }

    @Override
    public SpotifyToken refreshAccessToken() {
        if (token == null || token.getRefreshToken() == null) {
            log.error("No refresh token available. Please re-authenticate.");
            throw new IllegalStateException("No refresh token available. Please re-authenticate.");
        }

        try {
            log.info("Starting token refresh");
            Map<String, String> headers = createAuthHeaders();
            Map<String, String> formData = Map.of(
                    "grant_type", "refresh_token",
                    "refresh_token", token.getRefreshToken()
            );

            TokenResponse response = spotifyHttpClient.post(
                    "https://accounts.spotify.com/api/token",
                    headers,
                    formData,
                    TokenResponse.class
            );
            log.info("Received refresh response: {}", response);

            SpotifyToken newToken = new SpotifyToken(
                    response.getAccessToken(),
                    response.getRefreshToken(),
                    response.getTokenType(),
                    response.getExpiresIn(),
                    response.getScope(),
                    Instant.now()
            );
            this.token = newToken;
            log.info("Token refreshed successfully: {}", newToken);

            if (tokenUpdateListener != null) {
                tokenUpdateListener.onTokenUpdated(newToken);
            }

            setHeaders();
            return newToken;
        } catch (IOException e) {
            log.error("Failed to refresh token", e);
            throw new RuntimeException("Failed to refresh token", e);
        }
    }

    private <T> T withAuthenticatedAccess(Supplier<T> action) {
        validateAuthentication();
        ensureTokenIsValid();
        return action.get();
    }

    private void ensureTokenIsValid() {
        if (isExpired()) {
            refreshAccessToken();
        }
    }

    private void validateAuthentication() {
        if (!isAuthenticated) {
            throw new IllegalStateException("Client is not authenticated. Please call authenticate() before accessing data.");
        }
    }

    @Override
    public SpotifyUser getCurrentUserDetails() {
        return withAuthenticatedAccess(spotifyUserService::getUserDetails);
    }

    @Override
    public List<SpotifyTrack> getTopTracksLast4Weeks(int limit, int offset) {
        return withAuthenticatedAccess(() -> spotifyTracksService.getTopItems(TimeRange.SHORT_TERM, limit, offset));
    }

    @Override
    public List<SpotifyTrack> getTopTracksLast6Months(int limit, int offset) {
        return withAuthenticatedAccess(() -> spotifyTracksService.getTopItems(TimeRange.MEDIUM_TERM, limit, offset));
    }

    @Override
    public List<SpotifyTrack> getTopTracksAllTime(int limit, int offset) {
        return withAuthenticatedAccess(() -> spotifyTracksService.getTopItems(TimeRange.LONG_TERM, limit, offset));
    }

    @Override
    public List<SpotifyArtist> getTopArtistsLast4Weeks(int limit, int offset) {
        return withAuthenticatedAccess(() -> spotifyArtistsService.getTopItems(TimeRange.SHORT_TERM, limit, offset));
    }

    @Override
    public List<SpotifyArtist> getTopArtistsLast6Months(int limit, int offset) {
        return withAuthenticatedAccess(() -> spotifyArtistsService.getTopItems(TimeRange.MEDIUM_TERM, limit, offset));
    }

    @Override
    public List<SpotifyArtist> getTopArtistsAllTime(int limit, int offset) {
        return withAuthenticatedAccess(() -> spotifyArtistsService.getTopItems(TimeRange.LONG_TERM, limit, offset));
    }

//    public static class Builder {
//        private String clientId;
//        private String clientSecret;
//        private String redirectUri;
//        private String[] scopes;
//
//        public Builder withClientId(String clientId){
//            this.clientId = clientId;
//            return this;
//        }
//        public Builder withClientSecret(String clientSecret){
//            this.clientSecret = clientSecret;
//            return this;
//        }
//        public Builder withRedirectUri(String redirectUri){
//            this.redirectUri = redirectUri;
//            return this;
//        }
//        public Builder withScopes(String[] scopes){
//            this.scopes = scopes;
//            return this;
//        }
//
//        public SpotifyClientI build(){
//            validateConfig();
//            return new SpotifyClientI(this);
//        }
//
//        private void validateConfig() {
//            if(clientId == null || clientId.isEmpty()){
//                throw new IllegalStateException("clientId must be provided!");
//            }
//            if(clientSecret == null || clientSecret.isEmpty()){
//                throw new IllegalStateException("clientSecret must be provided!");
//            }
//            if(redirectUri == null || redirectUri.isEmpty()){
//                throw new IllegalStateException("redirectUri must be provided!");
//            }
//        }
//    }

}
