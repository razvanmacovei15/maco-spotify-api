package com.maco.spotify.api.config;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Getter
public class SpotifyConfig {
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String[] scopes;

    private SpotifyConfig(Builder builder){
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.redirectUri = builder.redirectUri;
        this.scopes = builder.scopes;
    }

    public String createAuthorizationUrl() {
        try {
            String encodedScopes = URLEncoder.encode(String.join(" ", scopes), StandardCharsets.UTF_8);
            String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);

            return "https://accounts.spotify.com/authorize" +
                    "?client_id=" + clientId +
                    "&response_type=code" +
                    "&redirect_uri=" + encodedRedirectUri +
                    "&scope=" + encodedScopes;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create authorization URL", e);
        }
    }
    public String createAuthorizationUrlWithState(String state) {
        try {
            String encodedScopes = URLEncoder.encode(String.join(" ", scopes), StandardCharsets.UTF_8);
            String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
            String encodedState = URLEncoder.encode(state, StandardCharsets.UTF_8);

            return "https://accounts.spotify.com/authorize" +
                    "?client_id=" + clientId +
                    "&response_type=code" +
                    "&redirect_uri=" + encodedRedirectUri +
                    "&scope=" + encodedScopes +
                    "&state=" + encodedState;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create authorization URL", e);
        }
    }

    public static SpotifyConfig fromProperties(String propertiesPath) {
        Properties props = new Properties();
        try (InputStream input = SpotifyConfig.class.getClassLoader().getResourceAsStream(propertiesPath)) {
            if (input == null) {
                throw new IllegalStateException("Unable to find " + propertiesPath);
            }
            props.load(input);

            return new Builder()
                    .withClientId(props.getProperty("spotify.client.id"))
                    .withClientSecret(props.getProperty("spotify.client.secret"))
                    .withRedirectUri(props.getProperty("spotify.redirect.uri"))
                    .withScopes(props.getProperty("spotify.scopes", "").split(","))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from " + propertiesPath, e);
        }
    }

    public static SpotifyConfig fromProperties() {
        return fromProperties("spotify.properties");
    }

    public static class Builder {
        private String clientId;
        private String clientSecret;
        private String redirectUri;
        private String[] scopes;

        public Builder withClientId(String clientId){
            this.clientId = clientId;
            return this;
        }
        public Builder withClientSecret(String clientSecret){
            this.clientSecret = clientSecret;
            return this;
        }
        public Builder withRedirectUri(String redirectUri){
            this.redirectUri = redirectUri;
            return this;
        }
        public Builder withScopes(String... scopes){
            this.scopes = scopes;
            return this;
        }

        public SpotifyConfig build(){
            validateConfig();
            return new SpotifyConfig(this);
        }

        private void validateConfig() {
            if(clientId == null || clientId.isEmpty()){
                throw new IllegalStateException("clientId must be provided!");
            }
            if(clientSecret == null || clientSecret.isEmpty()){
                throw new IllegalStateException("clientSecret must be provided!");
            }
            if(redirectUri == null || redirectUri.isEmpty()){
                throw new IllegalStateException("redirectUri must be provided!");
            }

        }
    }
}
