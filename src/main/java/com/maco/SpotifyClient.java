package com.maco;

import com.maco.api.TimeRange;
import com.maco.api.services.TrackService;
import com.maco.api.services.ArtistService;
import com.maco.auth.config.SpotifyConfig;
import com.maco.auth.callbackserver.AuthService;
import com.maco.auth.token.TokenManager;
import com.maco.model.Track;
import com.maco.model.Artist;
import com.maco.utils.Browser;

import java.util.List;

public class SpotifyClient {
    private final SpotifyConfig clientConfig;
    private final TokenManager tokenManager;
    private final TrackService trackService;
    private final ArtistService artistService;
    private boolean isAuthenticated = false;

    public SpotifyClient(SpotifyConfig clientConfig) {
        this.clientConfig = clientConfig;
        this.tokenManager = new TokenManager(clientConfig);
        this.trackService = new TrackService(tokenManager);
        this.artistService = new ArtistService(tokenManager);
    }

    public void authenticate() {
        try {
            // 1. Create authorization URL
            String authUrl = clientConfig.createAuthorizationUrl();
            Browser.openUrl(authUrl);

            // 2. Wait for callback and get authorization code
            String code = AuthService.callbackServer();

            // 3. Exchange code for tokens
            tokenManager.authenticate(code);

            isAuthenticated = true;
            System.out.println("Successfully authenticated with Spotify!");

        } catch (Exception e) {
            throw new RuntimeException("Failed to authenticate with Spotify", e);
        }
    }

    public List<Track> getTopTracksLast4Weeks(int limit, int offset) {
        if (!isAuthenticated) {
            throw new IllegalStateException("Client is not authenticated. Please call authenticate() first.");
        }
        return trackService.getTopItems(TimeRange.SHORT_TERM, limit, offset);
    }

    public List<Track> getTopTracksLast6Months(int limit, int offset){
        if(!isAuthenticated) {
            throw new IllegalStateException("Client is not authenticated. Please call authenticate() first.");
        }
        return trackService.getTopItems(TimeRange.MEDIUM_TERM, limit, offset);
    }

    public List<Track> getTopTracksAllTime(int limit, int offset){
        if(!isAuthenticated) {
            throw new IllegalStateException("Client is not authenticated. Please call authenticate() first.");
        }
        return trackService.getTopItems(TimeRange.LONG_TERM, limit, offset);
    }

    public List<Artist> getTopArtistsLast4Weeks(int limit, int offset) {
        if (!isAuthenticated) {
            throw new IllegalStateException("Client is not authenticated. Please call authenticate() first.");
        }
        return artistService.getTopItems(TimeRange.SHORT_TERM, limit, offset);
    }

    public List<Artist> getTopArtistsLast6Months(int limit, int offset) {
        if (!isAuthenticated) {
            throw new IllegalStateException("Client is not authenticated. Please call authenticate() first.");
        }
        return artistService.getTopItems(TimeRange.MEDIUM_TERM, limit, offset);
    }

    public List<Artist> getTopArtistsAllTime(int limit, int offset) {
        if (!isAuthenticated) {
            throw new IllegalStateException("Client is not authenticated. Please call authenticate() first.");
        }
        return artistService.getTopItems(TimeRange.LONG_TERM, limit, offset);
    }
}
