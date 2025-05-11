package com.maco.client.v2.model.extra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Represents external URLs for a Spotify object, such as a link to the Spotify web player.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalUrls {

    /**
     * Default constructor for the ExternalUrls class.
     * This constructor is used by Jackson for deserialization.
     */
    public ExternalUrls() {
    }

    /**
     * The external URL linking to the Spotify web player for this object.
     */
    @JsonProperty("spotify")
    private String spotify;
}
