package com.maco.client.v2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maco.client.v2.SpotifyToken;
import com.maco.client.v2.interfaces.TokenUpdateListener;
import com.maco.client.v2.utils.SpotifyHttpClient;
import lombok.Setter;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

/**
 * An abstract base service class for interacting with the Spotify Web API.
 * Provides common functionality such as HTTP GET requests and authentication header generation.
 */
public abstract class AbstractSpotifyService {
    /**
     * ObjectMapper used for serializing and deserializing JSON data.
     */
    protected final ObjectMapper objectMapper;

    /**
     * HTTP client wrapper used to communicate with the Spotify API.
     */
    protected final SpotifyHttpClient spotifyHttpClient;

    /**
     * Spotify API client ID.
     */
    protected final String clientId;

    /**
     * Spotify API client secret.
     */
    protected final String clientSecret;

    /**
     * HTTP headers used for requests. Can be set by subclasses.
     */
    @Setter
    protected Map<String, String> headers;

    /**
     * Constructs the base Spotify service with required dependencies.
     *
     * @param spotifyHttpClient the HTTP client for performing requests
     * @param clientId          Spotify application client ID
     * @param clientSecret      Spotify application client secret
     * @param headers           initial headers to use for requests
     */
    protected AbstractSpotifyService(SpotifyHttpClient spotifyHttpClient, String clientId, String clientSecret, Map<String, String> headers) {
        this.objectMapper = new ObjectMapper();
        this.spotifyHttpClient = spotifyHttpClient;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.headers = headers;
    }

    /**
     * Performs a GET request to the given Spotify API URL and maps the JSON response to the provided class type.
     *
     * @param url          the Spotify API endpoint URL
     * @param responseType the class of the response type
     * @param headers      headers to include in the request
     * @param <T>          the generic type of the response
     * @return the deserialized response
     * @throws IOException if the request fails or response parsing fails
     */
    protected <T> T get(String url, Class<T> responseType, Map<String, String> headers) throws IOException {
        String response = spotifyHttpClient.get(url, headers);
        return objectMapper.readValue(response, responseType);
    }

    /**
     * Generates basic authentication headers using client ID and client secret.
     *
     * @return a map containing the Authorization header
     */
    private Map<String, String> createAuthHeaders() {
        return Map.of(
                "Authorization", "Basic " + Base64.getEncoder().encodeToString(
                        (clientId + ":" + clientSecret).getBytes()
                )
        );
    }

    public void setToken(SpotifyToken token) {
        spotifyHttpClient.setCurrentToken(token);
        // Update headers with new token
        headers = Map.of(
            "Authorization", token.getAuthorizationHeader()
        );
    }

    public void setTokenUpdateListener(TokenUpdateListener listener) {
        spotifyHttpClient.setTokenUpdateListener(listener);
    }
}
