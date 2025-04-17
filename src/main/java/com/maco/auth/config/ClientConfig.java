package com.maco.auth.config;

import lombok.Getter;

@Getter
public class ClientConfig {
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    private ClientConfig(Builder builder){
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.redirectUri = builder.redirectUri;
    }

    public static class Builder {
        private String clientId;
        private String clientSecret;
        private String redirectUri;

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

        public ClientConfig build(){
            validateConfig();
            return new ClientConfig(this);
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
