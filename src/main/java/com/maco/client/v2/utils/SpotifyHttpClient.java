package com.maco.client.v2.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.maco.client.v2.SpotifyToken;
import com.maco.client.v2.interfaces.TokenUpdateListener;
import com.maco.client.v2.model.response.TokenResponse;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A helper class that wraps HTTP requests to the Spotify Web API,
 * including support for GET and POST operations with JSON/form-data handling.
 */
public class SpotifyHttpClient {

    private static HttpClient client;
    private final String clientId;
    private final String clientSecret;

    /**
     * Shared ObjectMapper configured with SNAKE_CASE naming strategy.
     */
    @Getter
    private static ObjectMapper objectMapper;

    @Setter
    private TokenUpdateListener tokenUpdateListener;
    @Setter
    private SpotifyToken currentToken;

    /**
     * Initializes the HTTP client and configures the JSON mapper.
     */
    public SpotifyHttpClient(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    private boolean isTokenExpired() {
        if (currentToken == null || currentToken.getCreatedAt() == null) {
            return true;
        }
        return Instant.now().isAfter(
            currentToken.getCreatedAt().plusSeconds(currentToken.getExpiresIn() - 60)
        );
    }

    private void refreshToken() throws IOException {
        if (currentToken == null || currentToken.getRefreshToken() == null) {
            throw new IOException("No refresh token available");
        }

        Map<String, String> headers = Map.of(
            "Content-Type", "application/x-www-form-urlencoded",
            "Authorization", "Basic " + Base64.getEncoder().encodeToString(
                (clientId + ":" + clientSecret).getBytes()
            )
        );

        Map<String, String> formData = Map.of(
            "grant_type", "refresh_token",
            "refresh_token", currentToken.getRefreshToken()
        );

        TokenResponse response = post(
            SpotifyConstants.AUTH_BASE_URL + "/api/token",
            headers,
            formData,
            TokenResponse.class
        );

        SpotifyToken newToken = new SpotifyToken(
            response.getAccessToken(),
            currentToken.getRefreshToken(), // Keep the existing refresh token
            response.getTokenType(),
            response.getExpiresIn(),
            response.getScope(),
            Instant.now()
        );

        currentToken = newToken;
        if (tokenUpdateListener != null) {
            tokenUpdateListener.onTokenUpdated(newToken);
        }
    }

    /**
     * Sends a POST request with form-encoded data and maps the response to a specified class.
     *
     * @param url          the request URL
     * @param headers      HTTP headers to include
     * @param formData     key-value pairs to be sent in the request body as x-www-form-urlencoded
     * @param responseType the class to which the response body will be deserialized
     * @param <T>          the type of the response object
     * @return the deserialized response object
     * @throws IOException if the request fails or JSON mapping fails
     */
    public <T> T post(String url, Map<String, String> headers, Map<String, String> formData, Class<T> responseType) throws IOException {
        String formDataString = formData.entrySet().stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(formDataString))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/x-www-form-urlencoded");

        headers.forEach(requestBuilder::header);

        HttpRequest request = requestBuilder.build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException("Unexpected response: " + response.statusCode() + " " + response.body());
            }
            return objectMapper.readValue(response.body(), responseType);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request was interrupted", e);
        }
    }

    /**
     * Sends a GET request to the specified URL and returns the raw response body.
     *
     * @param url     the request URL
     * @param headers HTTP headers to include
     * @return the response body as a String
     * @throws IOException if the request fails
     */
    public String get(String url, Map<String, String> headers) throws IOException {
        if (isTokenExpired()) {
            refreshToken();
        }

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(30));

        headers.forEach(requestBuilder::header);

        HttpRequest request = requestBuilder.build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 401) {
                // Token might have expired between check and request
                refreshToken();
                // Retry the request with new token
                return get(url, headers);
            }
            if (response.statusCode() != 200) {
                throw new IOException("Unexpected response: " + response.statusCode() + " " + response.body());
            }
            return response.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request was interrupted", e);
        }
    }
}
