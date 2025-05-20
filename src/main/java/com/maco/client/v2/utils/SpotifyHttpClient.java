package com.maco.client.v2.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.maco.client.v2.SpotifyClient;
import lombok.Getter;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A helper class that wraps HTTP requests to the Spotify Web API,
 * including support for GET and POST operations with JSON/form-data handling.
 */
public class SpotifyHttpClient {

    private static HttpClient client;

    /**
     * Shared ObjectMapper configured with SNAKE_CASE naming strategy.
     */
    @Getter
    private static ObjectMapper objectMapper;
    private final SpotifyClient spotifyClient;

    /**
     * Initializes the HTTP client and configures the JSON mapper.
     */
    public SpotifyHttpClient(SpotifyClient spotifyClient) {
        this.spotifyClient = spotifyClient;
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
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
        try {
            return executeGet(url, headers);
        } catch (IOException e) {
            if (e.getMessage().contains("401")) {
                spotifyClient.refreshAccessToken(); // this also updates headers internally
                Map<String, String> updatedHeaders = new HashMap<>(headers);
                updatedHeaders.put("Authorization", spotifyClient.getAccessToken().getAuthorizationHeader());
                return executeGet(url, updatedHeaders);
            }
            throw e;
        }
    }

    private String executeGet(String url, Map<String, String> headers) throws IOException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(30));

        headers.forEach(requestBuilder::header);

        HttpRequest request = requestBuilder.build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
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
