package com.maco.api.services;

import com.maco.api.TimeRange;
import com.maco.api.interfaces.UserTopItems;
import com.maco.auth.token.TokenManager;
import com.maco.model.Track;
import com.maco.model.spotifyresponse.TracksResponse;
import com.maco.service.SpotifyService;
import com.maco.utils.SpotifyConstants;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

public class TrackService extends SpotifyService implements UserTopItems<Track> {
    public TrackService(TokenManager tokenManager) {
        super(tokenManager);
    }

    @Override
    public List<Track> getTopItems(TimeRange timeRange, int limit, int offset) {
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
