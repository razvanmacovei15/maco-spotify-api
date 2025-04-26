package com.maco.client.v2.service;



import com.maco.client.v2.enums.TimeRange;
import com.maco.client.v2.model.SpotifyTrack;
import com.maco.client.v2.model.response.TracksResponse;
import com.maco.client.v2.utils.SpotifyConstants;
import com.maco.client.v2.utils.SpotifyHttpClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SpotifyTracksService extends AbstractSpotifyService {


    public SpotifyTracksService(SpotifyHttpClient spotifyHttpClient, String clientId, String clientSecret, Map<String, String> headers) {
        super(spotifyHttpClient, clientId, clientSecret, headers);
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

            TracksResponse response = get(url, TracksResponse.class, headers);
            return Arrays.asList(response.getItems());
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch top tracks", e);
        }
    }
}
