package com.maco.spotify.auth.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class SpotifyHttpClient {
    private final HttpClient client;

    @Getter
    private final ObjectMapper objectMapper;

    public SpotifyHttpClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    public <T> T post(String url, Map<String, String> headers, Map<String, String> formData, Class<T> responseType) throws IOException {
        String formDataString = formData.entrySet().stream()
                .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(formDataString))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/x-www-form-urlencoded");

        headers.forEach(requestBuilder::header);
        HttpRequest request = requestBuilder.build();

        try {
            log.debug("Sending POST request to {} with form data: {}", url, formData);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.error("POST request to {} failed with status {} and body: {}", url, response.statusCode(), response.body());
                throw new IOException("Unexpected response: " + response.statusCode() + " " + response.body());
            }

            log.debug("POST response from {}: {}", url, response.body());
            return objectMapper.readValue(response.body(), responseType);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("POST request to {} was interrupted", url, e);
            throw new IOException("Request was interrupted", e);
        } catch (IOException e) {
            log.error("POST request to {} failed", url, e);
            throw e;
        }
    }

    public String get(String url, Map<String, String> headers) throws IOException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(30));

        headers.forEach(requestBuilder::header);
        HttpRequest request = requestBuilder.build();

        try {
            log.debug("Sending GET request to {}", url);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.error("GET request to {} failed with status {} and body: {}", url, response.statusCode(), response.body());
                throw new IOException("Unexpected response: " + response.statusCode() + " " + response.body());
            }

            log.debug("GET response from {}: {}", url, response.body());
            return response.body();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("GET request to {} was interrupted", url, e);
            throw new IOException("Request was interrupted", e);
        } catch (IOException e) {
            log.error("GET request to {} failed", url, e);
            throw e;
        }
    }
}
