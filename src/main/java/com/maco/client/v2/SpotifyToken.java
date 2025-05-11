package com.maco.client.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Represents a Spotify access token and related authentication metadata.
 * Used to authorize requests to the Spotify Web API.
 */
@Data
@AllArgsConstructor
public class SpotifyToken {
    /**
     * Default constructor for the SpotifyToken class.
     * This constructor is used by Jackson for deserialization.
     */
    public SpotifyToken() {
    }

    /**
     * The access token provided by Spotify.
     */
    private String accessToken;

    /**
     * The refresh token used to obtain new access tokens.
     */
    private String refreshToken;

    /**
     * The token type (usually "Bearer").
     */
    private String tokenType;

    /**
     * The duration in seconds for which the access token is valid.
     */
    private long expiresIn;

    /**
     * The scope(s) granted with the token, space-separated.
     */
    private String scope;

    /**
     * The timestamp when the token was issued or refreshed.
     */
    private Instant createdAt;

    /**
     * Returns the HTTP Authorization header value using the access token.
     *
     * @return a string in the format "Bearer &lt;accessToken&gt;"
     */
    public String getAuthorizationHeader() {
        return tokenType + " " + accessToken;
    }
}
