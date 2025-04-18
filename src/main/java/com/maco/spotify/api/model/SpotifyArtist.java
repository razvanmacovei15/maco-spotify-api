package com.maco.spotify.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maco.spotify.api.model.extra.ExternalUrls;
import com.maco.spotify.api.model.extra.Followers;
import com.maco.spotify.api.model.extra.Image;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyArtist {
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

    @JsonProperty("followers")
    private Followers followers;

    @JsonProperty("genres")
    private String[] genres;

    @JsonProperty("images")
    private Image[] images;

    @JsonProperty("popularity")
    private int popularity;
}
