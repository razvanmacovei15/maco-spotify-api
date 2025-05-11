package com.maco.client.v2.config;

import com.maco.client.v2.SpotifyClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Spring Boot auto-configuration class for creating a {@link SpotifyClient} bean.
 * Loads properties from {@link SpotifyClientProperties} and instantiates the client
 * only if no other bean of type {@code SpotifyClient} is already defined.
 */
@Configuration
@EnableConfigurationProperties(SpotifyClientProperties.class)
public class SpotifyClientAutoConfiguration {
    /**
     * Default constructor for {@link SpotifyClientAutoConfiguration}.
     * Automatically invoked by Spring Boot during application startup.
     */
    public SpotifyClientAutoConfiguration() {
        // Default constructor intentionally empty
    }

    /**
     * Creates a {@link SpotifyClient} bean using values from {@link SpotifyClientProperties}.
     * This bean will only be registered if one does not already exist in the context.
     *
     * @param properties the properties used to configure the client
     * @return a new {@link SpotifyClient} instance
     */
    @Bean
    @ConditionalOnMissingBean
    public SpotifyClient spotifyClient(SpotifyClientProperties properties) {
        return new SpotifyClient(
                properties.getClientId(),
                properties.getClientSecret(),
                properties.getRedirectUri(),
                properties.getScopes()
        );
    }
}
