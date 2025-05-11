package com.maco.client.v2.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Represents the response received from Spotify's token endpoint.
 * Contains access and refresh token details, along with expiration and scope information.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {

    /**
     * Default constructor for the TokenResponse class.
     * This constructor is used by Jackson for deserialization.
     */
    public TokenResponse() {
    }

    /**
     * The access token issued by Spotify.
     */
    @JsonProperty
    private String accessToken;

    /**
     * The refresh token that can be used to obtain new access tokens.
     */
    @JsonProperty
    private String refreshToken;

    /**
     * The lifetime in seconds of the access token.
     */
    @JsonProperty
    private long expiresIn;

    /**
     * The type of the token issued (usually "Bearer").
     */
    @JsonProperty
    private String tokenType;

    /**
     * A space-separated list of scopes granted by the user.
     */
    @JsonProperty("scope")
    private String scope;

    @Override
    public String toString() {
        return "TokenResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", tokenType='" + tokenType + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
