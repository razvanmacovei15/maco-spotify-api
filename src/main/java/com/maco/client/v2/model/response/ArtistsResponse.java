package com.maco.client.v2.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maco.client.v2.model.SpotifyArtist;
import lombok.Getter;

/**
 * Represents a response from the Spotify Web API for a user's top artists.
 * Contains pagination information and an array of {@link SpotifyArtist} objects.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtistsResponse {

    /**
     * Default constructor for the ArtistsResponse class.
     * This constructor is used by Jackson for deserialization.
     */
    public ArtistsResponse() {
    }

    /**
     * The list of top artists returned in the current page.
     */
    @JsonProperty("items")
    private SpotifyArtist[] items;

    /**
     * The total number of items available across all pages.
     */
    @JsonProperty("total")
    private int total;

    /**
     * The maximum number of items returned per page.
     */
    @JsonProperty("limit")
    private int limit;

    /**
     * The index of the first item returned.
     */
    @JsonProperty("offset")
    private int offset;
}
