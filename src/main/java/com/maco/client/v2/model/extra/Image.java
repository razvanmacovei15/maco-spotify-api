package com.maco.client.v2.model.extra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Represents an image object returned by the Spotify Web API.
 * Commonly used for album covers, artist pictures, and profile photos.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {

    /**
     * Default constructor for the Image class.
     * This constructor is used by Jackson for deserialization.
     */
    public Image() {
    }

    /**
     * The source URL of the image.
     */
    @JsonProperty("url")
    private String url;

    /**
     * The image height in pixels.
     */
    @JsonProperty("height")
    private int height;

    /**
     * The image width in pixels.
     */
    @JsonProperty("width")
    private int width;
}
