package com.maco.client.v2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maco.client.v2.model.extra.Album;
import com.maco.client.v2.model.extra.ExternalUrls;
import lombok.Getter;

/**
 * Represents a Spotify track object as returned by the Spotify Web API.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyTrack {

    /**
     * Default constructor for the SpotifyTrack class.
     * This constructor is used by Jackson for deserialization.
     */
    public SpotifyTrack() {
    }

    /**
     * The unique identifier for the track.
     */
    @JsonProperty("id")
    private String id;

    /**
     * The name of the track.
     */
    @JsonProperty("name")
    private String name;

    /**
     * The object type (should be "track").
     */
    @JsonProperty("type")
    private String type;

    /**
     * The Spotify URI for the track.
     */
    @JsonProperty("uri")
    private String uri;

    /**
     * A link to the Web API endpoint providing full details of the track.
     */
    @JsonProperty("href")
    private String href;

    /**
     * External URLs for this track (e.g., Spotify web player link).
     */
    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;

    /**
     * The artists who performed the track.
     */
    @JsonProperty("artists")
    private SpotifyArtist[] spotifyArtists;

    /**
     * The album on which the track appears.
     */
    @JsonProperty("album")
    private Album album;

    /**
     * The track length in milliseconds.
     */
    @JsonProperty("duration_ms")
    private long durationMs;

    /**
     * Whether the track has explicit lyrics (true = yes).
     */
    @JsonProperty("explicit")
    private boolean explicit;

    /**
     * The popularity of the track (0 to 100).
     */
    @JsonProperty("popularity")
    private int popularity;

    /**
     * A link to a 30-second preview (MP3 format) of the track.
     */
    @JsonProperty("preview_url")
    private String previewUrl;

    /**
     * The number of the track on the album.
     */
    @JsonProperty("track_number")
    private int trackNumber;

    /**
     * The disc number (usually 1 unless the album has multiple discs).
     */
    @JsonProperty("disc_number")
    private int discNumber;

    /**
     * Whether the track is stored locally (true = local).
     */
    @JsonProperty("is_local")
    private boolean isLocal;
}
