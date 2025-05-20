package com.maco.client.v2;

import com.maco.client.v2.interfaces.TokenUpdateListener;
import com.maco.client.v2.model.SpotifyArtist;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SpotifyTokenRefreshTest {

    private SpotifyClient spotifyClient;
    private static final String CLIENT_ID = "40f0faeac8b043ee99f7bd42e134f97c";
    private static final String CLIENT_SECRET = "9713d372e12e4c699accf979bd406435";
    private static final String REDIRECT_URI = "http://localhost:8080/callback";
    private static final String[] SCOPES = {
        "user-read-private",
        "user-read-email",
        "user-top-read",
        "user-read-recently-played"
    };

    @BeforeEach
    void setUp() {
        spotifyClient = new SpotifyClient(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, SCOPES);
    }

    @Test
    void testTokenRefresh() throws InterruptedException {
        // Create a latch to wait for token refresh
        CountDownLatch tokenRefreshLatch = new CountDownLatch(1);
        
        // Set up token update listener
        spotifyClient.setTokenUpdateListener(newToken -> {
            log.info("Token refreshed! New token: {}", newToken);
            tokenRefreshLatch.countDown();
        });

        // Create an expired token
        SpotifyToken expiredToken = new SpotifyToken(
            "expired_access_token",
            "valid_refresh_token",
            "Bearer",
            3600,
            String.join(" ", SCOPES),
            Instant.now().minusSeconds(3601) // Token expired 1 second ago
        );

        // Set the expired token
        spotifyClient.setToken(expiredToken);

        try {
            // Try to search for an artist - this should trigger token refresh
            List<SpotifyArtist> artists = spotifyClient.searchForArtist("The Beatles");
            log.info("Found {} artists", artists.size());
        } catch (Exception e) {
            log.error("Error during artist search", e);
        }

        // Wait for token refresh (with timeout)
        boolean refreshed = tokenRefreshLatch.await(10, TimeUnit.SECONDS);
        if (!refreshed) {
            log.error("Token refresh did not occur within timeout period");
        }
    }

    @Test
    void testTokenRefreshWith401() throws InterruptedException {
        // Create a latch to wait for token refresh
        CountDownLatch tokenRefreshLatch = new CountDownLatch(1);
        
        // Set up token update listener
        spotifyClient.setTokenUpdateListener(newToken -> {
            log.info("Token refreshed after 401! New token: {}", newToken);
            tokenRefreshLatch.countDown();
        });

        // Create a token that's about to expire
        SpotifyToken token = new SpotifyToken(
            "valid_access_token",
            "valid_refresh_token",
            "Bearer",
            3600,
            String.join(" ", SCOPES),
            Instant.now()
        );

        // Set the token
        spotifyClient.setToken(token);

        try {
            // Try to search for an artist - this should trigger token refresh if 401 occurs
            List<SpotifyArtist> artists = spotifyClient.searchForArtist("The Beatles");
            log.info("Found {} artists", artists.size());
        } catch (Exception e) {
            log.error("Error during artist search", e);
        }

        // Wait for token refresh (with timeout)
        boolean refreshed = tokenRefreshLatch.await(10, TimeUnit.SECONDS);
        if (!refreshed) {
            log.error("Token refresh did not occur within timeout period");
        }
    }
} 