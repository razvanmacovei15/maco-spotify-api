package com.maco.spotify.api.client;

import com.maco.spotify.api.enums.TimeRange;
import com.maco.spotify.api.model.SpotifyUser;
import com.maco.spotify.api.service.impl.SpotifyTrackService;
import com.maco.spotify.api.service.impl.SpotifyArtistService;
import com.maco.spotify.api.config.SpotifyConfig;
import com.maco.spotify.api.service.impl.SpotifyUserService;
import com.maco.spotify.auth.callback.AuthCallbackService;
import com.maco.spotify.auth.token.TokenManager;
import com.maco.spotify.api.model.SpotifyTrack;
import com.maco.spotify.api.model.SpotifyArtist;
import com.maco.spotify.utils.Browser;
import lombok.Getter;

import java.util.List;
import java.util.function.Supplier;

public class SpotifyClient {
    @Getter
    private final long INACTIVITY_THRESHOLD = 3600000;
    private final SpotifyConfig clientConfig;
    @Getter
    private final TokenManager tokenManager;
    private final SpotifyTrackService spotifyTrackService;
    private final SpotifyArtistService spotifyArtistService;
    private final SpotifyUserService spotifyUserService;
    @Getter
    private boolean isAuthenticated = false;
    private long lastAccessTime;

    public SpotifyClient(SpotifyConfig clientConfig) {
        this.lastAccessTime = System.currentTimeMillis();
        this.clientConfig = clientConfig;
        this.tokenManager = new TokenManager(clientConfig);
        this.spotifyTrackService = new SpotifyTrackService(tokenManager);
        this.spotifyArtistService = new SpotifyArtistService(tokenManager);
        this.spotifyUserService = new SpotifyUserService(tokenManager);
    }

    public String getAuthUrl(){
        //good change?
        return clientConfig.createAuthorizationUrl();
    }

    public String getAuthUrlWithState(String state){
        return clientConfig.createAuthorizationUrlWithState(state);
    }

    private void updateLastAccessTime() {
        this.lastAccessTime = System.currentTimeMillis();
    }

    public boolean isActive(){
        return System.currentTimeMillis() - lastAccessTime < INACTIVITY_THRESHOLD;
    }

    public void authenticate() {
        try {
            String authUrl = clientConfig.createAuthorizationUrl();
            Browser.openUrl(authUrl);

            String code = AuthCallbackService.callbackServer();
            tokenManager.authenticate(code);

            isAuthenticated = true;
            updateLastAccessTime();
        } catch (Exception e) {
            throw new RuntimeException("Failed to authenticate with Spotify", e);
        }
    }

    public void authenticateWithCode(String code){
        tokenManager.authenticate(code);
        isAuthenticated = true;
        updateLastAccessTime();
    }

    public void deAuthenticate() {
        try {
            tokenManager.revokeToken();
            isAuthenticated = false;
            updateLastAccessTime();
        } catch (Exception e) {
            throw new RuntimeException("Failed to de-authenticate from Spotify", e);
        }
    }

    private void validateAuthentication() {
        if (!isAuthenticated) {
            throw new IllegalStateException("Client is not authenticated. Please call authenticate() before accessing data.");
        }
    }

    private void ensureTokenIsValid() {
        if (tokenManager.isTokenExpired()) {
            tokenManager.refreshToken();
        }
    }

    private <T> T withAuthenticatedAccess(Supplier<T> action) {
        validateAuthentication();
        ensureTokenIsValid();
        updateLastAccessTime();
        return action.get();
    }

    public SpotifyUser getUserDetails(){
        return withAuthenticatedAccess(spotifyUserService::getUserDetails);
    }

    // Top Tracks
    public List<SpotifyTrack> getTopTracksLast4Weeks(int limit, int offset) {
        return withAuthenticatedAccess(() -> spotifyTrackService.getTopItems(TimeRange.SHORT_TERM, limit, offset));
    }

    public List<SpotifyTrack> getTopTracksLast6Months(int limit, int offset) {
        return withAuthenticatedAccess(() -> spotifyTrackService.getTopItems(TimeRange.MEDIUM_TERM, limit, offset));
    }

    public List<SpotifyTrack> getTopTracksAllTime(int limit, int offset) {
        return withAuthenticatedAccess(() -> spotifyTrackService.getTopItems(TimeRange.LONG_TERM, limit, offset));
    }

    // Top Artists
    public List<SpotifyArtist> getTopArtistsLast4Weeks(int limit, int offset) {
        return withAuthenticatedAccess(() -> spotifyArtistService.getTopItems(TimeRange.SHORT_TERM, limit, offset));
    }

    public List<SpotifyArtist> getTopArtistsLast6Months(int limit, int offset) {
        return withAuthenticatedAccess(() -> spotifyArtistService.getTopItems(TimeRange.MEDIUM_TERM, limit, offset));
    }

    public List<SpotifyArtist> getTopArtistsAllTime(int limit, int offset) {
        return withAuthenticatedAccess(() -> spotifyArtistService.getTopItems(TimeRange.LONG_TERM, limit, offset));
    }
}
