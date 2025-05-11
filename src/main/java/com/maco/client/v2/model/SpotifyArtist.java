package com.maco.client.v2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maco.client.v2.model.extra.ExternalUrls;
import com.maco.client.v2.model.extra.Followers;
import com.maco.client.v2.model.extra.Image;
import lombok.Getter;

/**
 * Represents a Spotify artist object, as returned by the Spotify Web API.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyArtist {

    /**
     * Default constructor for the SpotifyArtist class.
     * This constructor is used by Jackson for deserialization.
     */
    public SpotifyArtist() {
    }

    /**
     * The unique identifier for the artist.
     */
    @JsonProperty("id")
    private String id;

    /**
     * The name of the artist.
     */
    @JsonProperty("name")
    private String name;

    /**
     * The object type (should be "artist").
     */
    @JsonProperty("type")
    private String type;

    /**
     * The Spotify URI for the artist.
     */
    @JsonProperty("uri")
    private String uri;

    /**
     * A link to the Web API endpoint providing full details of the artist.
     */
    @JsonProperty("href")
    private String href;

    /**
     * External URLs for this artist (e.g., Spotify web player link).
     */
    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;

    /**
     * Followers metadata for the artist.
     */
    @JsonProperty("followers")
    private Followers followers;

    /**
     * A list of genres associated with the artist.
     */
    @JsonProperty("genres")
    private String[] genres;

    /**
     * An array of the artist's profile images.
     */
    @JsonProperty("images")
    private Image[] images;

    /**
     * The popularity of the artist (0 to 100).
     */
    @JsonProperty("popularity")
    private int popularity;
}
