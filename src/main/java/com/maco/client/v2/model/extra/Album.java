package com.maco.client.v2.model.extra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maco.client.v2.model.SpotifyArtist;
import lombok.Getter;

/**
 * Represents a Spotify album object, as returned by the Spotify Web API.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Album {

    /**
     * Default constructor for the Album class.
     * This constructor is used by Jackson for deserialization.
     */
    public Album() {
    }

    /**
     * The unique identifier for the album.
     */
    @JsonProperty("id")
    private String id;

    /**
     * The name of the album.
     */
    @JsonProperty("name")
    private String name;

    /**
     * The object type (typically "album").
     */
    @JsonProperty("type")
    private String type;

    /**
     * The Spotify URI for the album.
     */
    @JsonProperty("uri")
    private String uri;

    /**
     * A link to the Web API endpoint providing full details of the album.
     */
    @JsonProperty("href")
    private String href;

    /**
     * External URLs for this album (e.g., Spotify web player link).
     */
    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;

    /**
     * An array of images representing the album.
     */
    @JsonProperty("images")
    private Image[] images;

    /**
     * The release date of the album.
     */
    @JsonProperty("release_date")
    private String releaseDate;

    /**
     * The precision with which {@code releaseDate} is known (e.g., "year", "month", "day").
     */
    @JsonProperty("release_date_precision")
    private String releaseDatePrecision;

    /**
     * The total number of tracks in the album.
     */
    @JsonProperty("total_tracks")
    private int totalTracks;

    /**
     * The artists who contributed to the album.
     */
    @JsonProperty("artists")
    private SpotifyArtist[] spotifyArtists;
}
