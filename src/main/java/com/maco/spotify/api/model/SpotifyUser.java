package com.maco.spotify.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maco.spotify.api.model.extra.ExternalUrls;
import com.maco.spotify.api.model.extra.Followers;
import com.maco.spotify.api.model.extra.Image;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyUser {
    @JsonProperty("id")
    private String id;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("href")
    private String href;

    @JsonProperty("type")
    private String type;

    @JsonProperty("country")
    private String country;

    @JsonProperty("product")
    private String product;

    @JsonProperty("followers")
    private Followers followers;

    @JsonProperty("images")
    private Image[] images;

    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;

}