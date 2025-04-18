package com.maco.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maco.auth.http.MyHttpClient;
import com.maco.auth.token.TokenManager;
import com.maco.model.User;
import com.maco.utils.SpotifyConstants;
import lombok.Getter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

public abstract class SpotifyService {
    protected final TokenManager tokenManager;
    protected final MyHttpClient httpClient;
    protected final ObjectMapper objectMapper;


    protected SpotifyService(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
        this.httpClient = new MyHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    protected <T> T get(String url, Class<T> responseType) throws IOException {
        String response = httpClient.get(url, createAuthHeaders());
        return objectMapper.readValue(response, responseType);
    }

    protected <T> T post(String url, Map<String, String> body, Map<String, String> headers, Class<T> responseType) throws IOException {
        String response = httpClient.post(url, body, headers, String.class);
        return objectMapper.readValue(response, responseType);
    }

    protected Map<String, String> createAuthHeaders() {
        return Map.of(
                "Authorization", tokenManager.getCurrentToken().getAuthorizationHeader()
        );
    }
}
