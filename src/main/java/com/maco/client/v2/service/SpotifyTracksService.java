package com.maco.client.v2.service;



import com.maco.client.v2.enums.TimeRange;
import com.maco.client.v2.model.SpotifyTrack;
import com.maco.client.v2.model.response.TracksResponse;
import com.maco.client.v2.utils.SpotifyConstants;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SpotifyTracksService extends AbstractSpotifyService {
    public SpotifyTracksService(String clientId, String clientSecret) {
        super(clientId, clientSecret);
    }

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
