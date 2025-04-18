package com.maco.spotify.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maco.spotify.api.model.extra.Album;
import com.maco.spotify.api.model.extra.ExternalUrls;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyTrack {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private String type;

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("href")
    private String href;

    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;

    @JsonProperty("artists")
    private SpotifyArtist[] spotifyArtists;

    @JsonProperty("album")
    private Album album;

    @JsonProperty("duration_ms")
    private long durationMs;

    @JsonProperty("explicit")
    private boolean explicit;

    @JsonProperty("popularity")
    private int popularity;

    @JsonProperty("preview_url")
    private String previewUrl;

    @JsonProperty("track_number")
    private int trackNumber;

    @JsonProperty("disc_number")
    private int discNumber;

    @JsonProperty("is_local")
    private boolean isLocal;
}