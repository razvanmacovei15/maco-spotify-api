package com.maco.client.v2.config;

import com.maco.client.v2.SpotifyClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Configuration
@EnableConfigurationProperties(SpotifyClientProperties.class)
public class SpotifyClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SpotifyClient spotifyClient(SpotifyClientProperties properties) {
        return new SpotifyClient(properties.getClientId(),
                properties.getClientSecret(),
                properties.getRedirectUri(),
                properties.getScopes());
    }
}
