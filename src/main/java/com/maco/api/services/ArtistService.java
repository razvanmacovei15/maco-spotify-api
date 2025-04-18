package com.maco.api.services;

import com.maco.api.TimeRange;
import com.maco.api.interfaces.UserTopItems;
import com.maco.auth.token.TokenManager;
import com.maco.model.Artist;
import com.maco.model.spotifyresponse.ArtistsResponse;
import com.maco.service.SpotifyService;
import com.maco.utils.SpotifyConstants;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ArtistService extends SpotifyService implements UserTopItems<Artist> {
    public ArtistService(TokenManager tokenManager) {
        super(tokenManager);
    }

    @Override
    public List<Artist> getTopItems(TimeRange tr, int limit, int offset) {
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
