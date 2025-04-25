package com.maco.client.v2.service;



import com.maco.client.v2.model.SpotifyUser;
import com.maco.client.v2.utils.SpotifyConstants;

import java.io.IOException;

public class SpotifyUserService extends AbstractSpotifyService{
    public SpotifyUserService(String clientId, String clientSecret) {
        super(clientId, clientSecret);
    }

    public SpotifyUser getUserDetails() {
        try {
            String url = SpotifyConstants.USER_DETAILS;
            return get(url, SpotifyUser.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch user details", e);
        }
    }
}
