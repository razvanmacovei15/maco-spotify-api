package com.maco.spotify.api.service.impl;

import com.maco.spotify.api.enums.TimeRange;
import com.maco.spotify.api.model.SpotifyTrack;
import com.maco.spotify.api.model.SpotifyUser;
import com.maco.spotify.api.model.response.TracksResponse;
import com.maco.spotify.api.service.SpotifyService;
import com.maco.spotify.auth.token.TokenManager;
import com.maco.spotify.utils.SpotifyConstants;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SpotifyUserService extends SpotifyService {

    public SpotifyUserService(TokenManager tokenManager) {
        super(tokenManager);
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
