package com.maco;

import com.maco.auth.config.SpotifyConfig;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        SpotifyConfig spotifyConfig = SpotifyConfig.fromProperties();

        SpotifyClient client = new SpotifyClient(spotifyConfig);
        client.authenticate();
    }
}