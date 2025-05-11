package com.maco.client.v2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Spotify client.
 * These properties are typically defined in an application's `application.yml` or `application.properties` file
 * using the `spotify` prefix.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spotify")
public class SpotifyClientProperties {

    /**
     * Default constructor for {@link SpotifyClientProperties}.
     * Automatically invoked by Spring Boot during application startup.
     */
    public SpotifyClientProperties() {
        // Default constructor intentionally empty
    }


    /**
     * The Spotify client ID used for authentication.
     */
    private String clientId;

    /**
     * The Spotify client secret used for authentication.
     */
    private String clientSecret;

    /**
     * The URI to which Spotify redirects after successful authentication.
     */
    private String redirectUri;

    /**
     * The scopes the application will request during authorization.
     */
    private String[] scopes;
}
