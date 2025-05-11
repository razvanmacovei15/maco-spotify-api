package com.maco.client.v2.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maco.client.v2.model.SpotifyTrack;
import lombok.Getter;

/**
 * Represents a response from the Spotify Web API for a user's top tracks.
 * Includes pagination metadata and an array of {@link SpotifyTrack} items.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TracksResponse {

    /**
     * Default constructor for the TracksResponse class.
     * This constructor is used by Jackson for deserialization.
     */
    public TracksResponse() {
    }

    /**
     * The list of top tracks returned in the current page.
     */
    @JsonProperty("items")
    private SpotifyTrack[] items;

    /**
     * The total number of available top tracks.
     */
    @JsonProperty("total")
    private int total;

    /**
     * The maximum number of tracks returned per page.
     */
    @JsonProperty("limit")
    private int limit;

    /**
     * The index of the first track in the returned page.
     */
    @JsonProperty("offset")
    private int offset;
}
