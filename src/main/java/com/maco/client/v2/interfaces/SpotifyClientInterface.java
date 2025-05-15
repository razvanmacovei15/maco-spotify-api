package com.maco.client.v2.interfaces;

import com.maco.client.v2.SpotifyToken;
import com.maco.client.v2.model.SpotifyArtist;
import com.maco.client.v2.model.SpotifyTrack;
import com.maco.client.v2.model.SpotifyUser;

import java.util.List;

/**
 * Defines the contract for a Spotify API client that handles authentication and data retrieval.
 */
public interface SpotifyClientInterface {

    /**
     * Checks if the client is currently authenticated with a valid token.
     *
     * @return true if authenticated, false otherwise
     */
    boolean isAuthenticated();

    /**
     * Generates the Spotify authorization URL to redirect users for OAuth login.
     *
     * @param state a unique state string to protect against CSRF
     * @return the full authorization URL
     */
    String getAuthorizationUrl(String state);

    /**
     * Exchanges the authorization code for an access token.
     *
     * @param code the authorization code returned by Spotify
     */
    void authenticate(String code);

    /**
     * Clears any stored access token or session data.
     */
    void deAuthenticate();

    /**
     * Retrieves the current Spotify access token details.
     *
     * @return a {@link SpotifyToken} object
     */
    SpotifyToken getAccessToken();

    /**
     * Manually sets the current access and refresh token details.
     *
     * @param accessToken  the access token
     * @param refreshToken the refresh token
     * @param tokenType    the type of token (e.g., Bearer)
     * @param expiresIn    expiration time in seconds
     * @param scope        the granted scopes
     */
    void setAccessToken(String accessToken, String refreshToken, String tokenType, int expiresIn, String scope);

    /**
     * Refreshes the access token using the stored refresh token.
     *
     * @return a new {@link SpotifyToken}
     */
    SpotifyToken refreshAccessToken();

    /**
     * Fetches the current user's Spotify profile information.
     *
     * @return a {@link SpotifyUser} object
     */
    SpotifyUser getCurrentUserDetails();

    /**
     * Retrieves the user's top tracks from the last 4 weeks.
     *
     * @param limit  maximum number of items to return
     * @param offset pagination offset
     * @return list of {@link SpotifyTrack}
     */
    List<SpotifyTrack> getTopTracksLast4Weeks(int limit, int offset);

    /**
     * Retrieves the user's top tracks from the last 6 months.
     *
     * @param limit  maximum number of items to return
     * @param offset pagination offset
     * @return list of {@link SpotifyTrack}
     */
    List<SpotifyTrack> getTopTracksLast6Months(int limit, int offset);

    /**
     * Retrieves the user's top tracks of all time.
     *
     * @param limit  maximum number of items to return
     * @param offset pagination offset
     * @return list of {@link SpotifyTrack}
     */
    List<SpotifyTrack> getTopTracksAllTime(int limit, int offset);

    /**
     * Retrieves the user's top artists from the last 4 weeks.
     *
     * @param limit  maximum number of items to return
     * @param offset pagination offset
     * @return list of {@link SpotifyArtist}
     */
    List<SpotifyArtist> getTopArtistsLast4Weeks(int limit, int offset);

    /**
     * Retrieves the user's top artists from the last 6 months.
     *
     * @param limit  maximum number of items to return
     * @param offset pagination offset
     * @return list of {@link SpotifyArtist}
     */
    List<SpotifyArtist> getTopArtistsLast6Months(int limit, int offset);

    /**
     * Retrieves the user's top artists of all time.
     *
     * @param limit  maximum number of items to return
     * @param offset pagination offset
     * @return list of {@link SpotifyArtist}
     */
    List<SpotifyArtist> getTopArtistsAllTime(int limit, int offset);
    /**
     * Searches for artists based on the provided name.
     *
     * @param artistName the name of the artist to search for
     * @return a list of {@link SpotifyArtist} matching the search criteria
     */
    List<SpotifyArtist> searchForArtist(String artistName);
}
