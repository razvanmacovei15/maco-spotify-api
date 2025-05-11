package com.maco.client.v2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maco.client.v2.model.extra.ExternalUrls;
import com.maco.client.v2.model.extra.Followers;
import com.maco.client.v2.model.extra.Image;
import lombok.Getter;

import java.util.Arrays;

/**
 * Represents a Spotify user profile, mapped from the Spotify Web API response.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyUser {

    /**
     * Default constructor for the SpotifyUser class.
     * This constructor is used by Jackson for deserialization.
     */
    public SpotifyUser() {
    }

    /**
     * The unique identifier for the Spotify user.
     */
    @JsonProperty("id")
    private String id;

    /**
     * The display name of the user.
     */
    @JsonProperty("display_name")
    private String displayName;

    /**
     * The user's email address.
     */
    @JsonProperty("email")
    private String email;

    /**
     * The Spotify URI for the user.
     */
    @JsonProperty("uri")
    private String uri;

    /**
     * A link to the Web API endpoint providing full details of the user.
     */
    @JsonProperty("href")
    private String href;

    /**
     * The object type: "user".
     */
    @JsonProperty("type")
    private String type;

    /**
     * The country of the user, identified by an ISO 3166-1 alpha-2 country code.
     */
    @JsonProperty("country")
    private String country;

    /**
     * The user's Spotify subscription type (e.g., free, premium).
     */
    @JsonProperty("product")
    private String product;

    /**
     * The number of followers the user has.
     */
    @JsonProperty("followers")
    private Followers followers;

    /**
     * An array of the user's profile images.
     */
    @JsonProperty("images")
    private Image[] images;

    /**
     * External URLs for the user, such as a link to their Spotify profile.
     */
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
