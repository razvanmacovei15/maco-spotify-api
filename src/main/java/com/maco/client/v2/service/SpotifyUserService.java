package com.maco.client.v2.service;

import com.maco.client.v2.model.SpotifyUser;
import com.maco.client.v2.utils.SpotifyConstants;
import com.maco.client.v2.utils.SpotifyHttpClient;

import java.io.IOException;
import java.util.Map;

/**
 * Service class for accessing Spotify user profile information.
 * Inherits common Spotify API functionality from {@link AbstractSpotifyService}.
 */
public class SpotifyUserService extends AbstractSpotifyService {

    /**
     * Constructs the SpotifyUserService with required dependencies.
     *
     * @param spotifyHttpClient the HTTP client used to interact with Spotify API
     * @param clientId          Spotify application client ID
     * @param clientSecret      Spotify application client secret
     * @param headers           initial headers to use for Spotify API requests
     */
    public SpotifyUserService(SpotifyHttpClient spotifyHttpClient, String clientId, String clientSecret, Map<String, String> headers) {
        super(spotifyHttpClient, clientId, clientSecret, headers);
    }

    /**
     * Fetches the current user's Spotify profile information.
     *
     * @return a {@link SpotifyUser} object containing profile details
     * @throws RuntimeException if the request or response parsing fails
     */
    public SpotifyUser getUserDetails() {
        try {
            String url = SpotifyConstants.USER_DETAILS;
            return get(url, SpotifyUser.class, headers);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch user details", e);
        }
    }
}
