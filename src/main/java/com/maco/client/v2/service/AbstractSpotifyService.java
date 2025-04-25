package com.maco.client.v2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maco.client.v2.SpotifyToken;
import com.maco.client.v2.utils.SpotifyHttpClient;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public abstract class AbstractSpotifyService {
    protected final ObjectMapper objectMapper;
    protected final String clientId;
    protected final String clientSecret;
    public AbstractSpotifyService(String clientId, String clientSecret) {
        this.objectMapper = new ObjectMapper();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    protected <T> T get(String url, Class<T> responseType) throws IOException {
        String response = SpotifyHttpClient.get(url, createAuthHeaders());
        return objectMapper.readValue(response, responseType);
    }

    private Map<String, String> createAuthHeaders() {
        return Map.of(
                "Authorization", "Basic " + Base64.getEncoder().encodeToString(
                        (clientId + ":" + clientSecret).getBytes()
                )
        );
    }
}
