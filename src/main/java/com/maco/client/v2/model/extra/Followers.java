package com.maco.client.v2.model.extra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Represents follower metadata for a Spotify user or artist.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Followers {

    /**
     * Default constructor for the Followers class.
     * This constructor is used by Jackson for deserialization.
     */
    public Followers() {
    }

    /**
     * A link to the Web API endpoint providing full details of the followers collection (currently null).
     */
    @JsonProperty("href")
    private String href;

    /**
     * The total number of followers.
     */
    @JsonProperty("total")
    private int total;
}
