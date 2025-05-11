package com.maco.client.v2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spotify")
public class SpotifyClientProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String[] scopes;
}
