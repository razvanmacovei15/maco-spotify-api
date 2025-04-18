package com.maco.spotify.api.service.impl;

import com.maco.spotify.api.enums.TimeRange;
import com.maco.spotify.api.service.UserTopItems;
import com.maco.spotify.auth.token.TokenManager;
import com.maco.spotify.api.model.SpotifyTrack;
import com.maco.spotify.api.model.response.TracksResponse;
import com.maco.spotify.api.service.SpotifyService;
import com.maco.spotify.utils.SpotifyConstants;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

public class SpotifyTrackService extends SpotifyService implements UserTopItems<SpotifyTrack> {
    public SpotifyTrackService(TokenManager tokenManager) {
        super(tokenManager);
    }

    @Override
    public List<SpotifyTrack> getTopItems(TimeRange timeRange, int limit, int offset) {
        try {
            String url = String.format("%s%s?time_range=%s&limit=%d&offset=%d",
                    SpotifyConstants.API_BASE_URL,
                    SpotifyConstants.USER_TOP_TRACKS_URL,
                    timeRange.getValue(),
                    limit,
                    offset
            );

            TracksResponse response = get(url, TracksResponse.class);
            return Arrays.asList(response.getItems());
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch top tracks", e);
        }
    }
}
