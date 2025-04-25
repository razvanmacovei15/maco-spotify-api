package com.maco.client.v2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maco.client.v2.model.extra.ExternalUrls;
import com.maco.client.v2.model.extra.Followers;
import com.maco.client.v2.model.extra.Image;
import lombok.Getter;

import java.util.Arrays;

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

    @Override
    public String toString() {
        return "SpotifyUser{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", uri='" + uri + '\'' +
                ", href='" + href + '\'' +
                ", type='" + type + '\'' +
                ", country='" + country + '\'' +
                ", product='" + product + '\'' +
                ", followers=" + followers +
                ", images=" + Arrays.toString(images) +
                ", externalUrls=" + externalUrls +
                '}';
    }
}