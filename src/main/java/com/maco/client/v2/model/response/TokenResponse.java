package com.maco.client.v2.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {
    @JsonProperty
    private String accessToken;
    @JsonProperty
    private String refreshToken;
    @JsonProperty
    private long expiresIn;
    @JsonProperty
    private String tokenType;
    @JsonProperty("scope")
    private String scope;

    @Override
    public String toString() {
        return "TokenResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", tokenType='" + tokenType + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
