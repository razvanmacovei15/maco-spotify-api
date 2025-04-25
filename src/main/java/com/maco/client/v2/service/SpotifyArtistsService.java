package com.maco.client.v2.service;



import com.maco.client.v2.enums.TimeRange;
import com.maco.client.v2.model.SpotifyArtist;
import com.maco.client.v2.model.response.ArtistsResponse;
import com.maco.client.v2.utils.SpotifyConstants;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SpotifyArtistsService extends AbstractSpotifyService{
    public SpotifyArtistsService(String clientId, String clientSecret) {
        super(clientId, clientSecret);
    }

    public List<SpotifyArtist> getTopItems(TimeRange timeRange, int limit, int offset) {
        try {
            String url = String.format("%s%s?time_range=%s&limit=%d&offset=%d",
                    SpotifyConstants.API_BASE_URL,
                    SpotifyConstants.USER_TOP_ARTISTS_URL,
                    timeRange.getValue(),
                    limit,
                    offset
            );

            ArtistsResponse response = get(url, ArtistsResponse.class);
            return Arrays.asList(response.getItems());
        } catch (IOException e){
            throw new RuntimeException("Failed to fetch top tracks", e);
        }
    }
}
