package com.maco.spotify.api.client;

import com.maco.spotify.api.enums.TimeRange;
import com.maco.spotify.api.service.impl.SpotifyTrackService;
import com.maco.spotify.api.service.impl.SpotifyArtistService;
import com.maco.spotify.api.config.SpotifyConfig;
import com.maco.spotify.auth.callback.AuthCallbackService;
import com.maco.spotify.auth.token.TokenManager;
import com.maco.spotify.api.model.SpotifyTrack;
import com.maco.spotify.api.model.SpotifyArtist;
import com.maco.spotify.utils.Browser;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
public class SpotifyClient {
    private final SpotifyConfig clientConfig;
    private final TokenManager tokenManager;
    private final SpotifyTrackService spotifyTrackService;
    private final SpotifyArtistService spotifyArtistService;
    private boolean isAuthenticated = false;

    public SpotifyClient(SpotifyConfig clientConfig) {
        this.clientConfig = clientConfig;
        this.tokenManager = new TokenManager(clientConfig);
        this.spotifyTrackService = new SpotifyTrackService(tokenManager);
        this.spotifyArtistService = new SpotifyArtistService(tokenManager);
    }

    public void authenticate() {
        try {
            String authUrl = clientConfig.createAuthorizationUrl();
            log.info("Opening Spotify authentication URL...");
            Browser.openUrl(authUrl);

            String code = AuthCallbackService.callbackServer();
            tokenManager.authenticate(code);

            isAuthenticated = true;
            log.info("Successfully authenticated with Spotify.");
        } catch (Exception e) {
            log.error("Failed to authenticate with Spotify", e);
            throw new RuntimeException("Failed to authenticate with Spotify", e);
        }
    }

    public void deAuthenticate() {
        try {
            tokenManager.revokeToken();
            isAuthenticated = false;
            log.info("Successfully de-authenticated from Spotify.");
        } catch (Exception e) {
            log.error("Failed to de-authenticate from Spotify", e);
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
            log.info("Spotify token expired. Attempting to refresh...");
            tokenManager.refreshToken();
            log.info("Token refreshed successfully.");
        }
    }

    private <T> T withAuthenticatedAccess(Supplier<T> action) {
        validateAuthentication();
        ensureTokenIsValid();
        return action.get();
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
