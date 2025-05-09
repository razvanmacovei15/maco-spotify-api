package com.maco.client.v2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maco.client.v2.utils.SpotifyHttpClient;
import lombok.Setter;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public abstract class AbstractSpotifyService {
    protected final ObjectMapper objectMapper;
    protected final SpotifyHttpClient spotifyHttpClient;
    protected final String clientId;
    protected final String clientSecret;
    @Setter
    protected Map<String, String> headers;

    public AbstractSpotifyService(SpotifyHttpClient spotifyHttpClient,String clientId, String clientSecret, Map<String, String> headers) {
        this.objectMapper = new ObjectMapper();
        this.spotifyHttpClient = spotifyHttpClient;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.headers = headers;
    }

    protected <T> T get(String url, Class<T> responseType, Map<String, String> headers) throws IOException {
        String response = spotifyHttpClient.get(url, headers);
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
