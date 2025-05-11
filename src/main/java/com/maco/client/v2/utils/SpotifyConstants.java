package com.maco.client.v2.utils;

/**
 * A utility class containing constant values for interacting with the Spotify Web API.
 * This class is not meant to be instantiated.
 */
public class SpotifyConstants {

    /**
     * Base URL for Spotify authorization requests.
     */
    public static final String AUTH_BASE_URL = "https://accounts.spotify.com";

    /**
     * URL for fetching the current user's profile information.
     */
    public static final String USER_DETAILS = "https://api.spotify.com/v1/me";

    /**
     * Base URL for general Spotify Web API requests.
     */
    public static final String API_BASE_URL = "https://api.spotify.com/v1";

    /**
     * Endpoint path for retrieving the current user's top tracks.
     */
    public static final String USER_TOP_TRACKS_URL = "/me/top/tracks";

    /**
     * Endpoint path for retrieving the current user's top artists.
     */
    public static final String USER_TOP_ARTISTS_URL = "/me/top/artists";

    /**
     * Private constructor to prevent instantiation.
     */
    private SpotifyConstants() {
    }
}
