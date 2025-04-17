package com.maco;

import com.maco.auth.service.AuthService;
import com.maco.auth.SpotifyAuthenticator;
import com.maco.auth.config.ClientConfig;
import com.maco.auth.model.AuthToken;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        System.out.println("Hello world!");
        ClientConfig clientConfig = new ClientConfig.Builder()
                .withClientId("40f0faeac8b043ee99f7bd42e134f97c")
                .withClientSecret("9713d372e12e4c699accf979bd406435")
                .withRedirectUri("http://localhost:8888/callback")
                .build();

        SpotifyAuthenticator auth = new SpotifyAuthenticator(clientConfig);

        String authUrl = auth.createAuthorizationUrl();
        System.out.println("Please visit: " + authUrl);

        String code = AuthService.callbackServer();
        System.out.println(code);

        AuthToken token = auth.authenticate(code);
        System.out.println("Yupiiii");
        System.out.println("Access token: " + token.getAccessToken());
    }
}