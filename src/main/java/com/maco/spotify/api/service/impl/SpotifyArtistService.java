package com.maco.spotify.api.service.impl;

import com.maco.spotify.api.enums.TimeRange;
import com.maco.spotify.api.service.UserTopItems;
import com.maco.spotify.auth.token.TokenManager;
import com.maco.spotify.api.model.SpotifyArtist;
import com.maco.spotify.api.model.response.ArtistsResponse;
import com.maco.spotify.api.service.SpotifyService;
import com.maco.spotify.utils.SpotifyConstants;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SpotifyArtistService extends SpotifyService implements UserTopItems<SpotifyArtist> {
    public SpotifyArtistService(TokenManager tokenManager) {
        super(tokenManager);
    }

    @Override
    public List<SpotifyArtist> getTopItems(TimeRange tr, int limit, int offset) {
        try {
            String url = String.format("%s%s?time_range=%s&limit=%d&offset=%d",
                    SpotifyConstants.API_BASE_URL,
                    SpotifyConstants.USER_TOP_ARTISTS_URL,
                    tr.getValue(),
                    limit, offset
            );

            ArtistsResponse response = get(url, ArtistsResponse.class);
            return Arrays.asList(response.getItems());
        } catch (IOException e){
                throw new RuntimeException("Failed to fetch top tracks", e);
        }
    }
}
