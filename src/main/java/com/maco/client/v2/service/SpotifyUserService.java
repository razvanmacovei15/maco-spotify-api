package com.maco.client.v2.service;



import com.maco.client.v2.model.SpotifyUser;
import com.maco.client.v2.utils.SpotifyConstants;
import com.maco.client.v2.utils.SpotifyHttpClient;

import java.io.IOException;
import java.util.Map;

public class SpotifyUserService extends AbstractSpotifyService{

    public SpotifyUserService(SpotifyHttpClient spotifyHttpClient, String clientId, String clientSecret, Map<String, String> headers) {
        super(spotifyHttpClient, clientId, clientSecret, headers);
    }

    public SpotifyUser getUserDetails() {
        try {
            String url = SpotifyConstants.USER_DETAILS;
            return get(url, SpotifyUser.class, headers);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch user details", e);
        }
    }
}
