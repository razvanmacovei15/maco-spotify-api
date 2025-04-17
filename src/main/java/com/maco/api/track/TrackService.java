package com.maco.api.track;

import com.maco.api.TimePeriod;
import com.maco.api.annotations.TimeRange;
import com.maco.api.interfaces.UserTopItems;
import com.maco.auth.token.TokenManager;
import com.maco.model.Track;
import com.maco.model.TracksResponse;
import com.maco.service.SpotifyService;
import com.maco.utils.SpotifyConstants;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Arrays;

public class TrackService extends SpotifyService implements UserTopItems<Track> {
    public TrackService(TokenManager tokenManager) {
        super(tokenManager);
    }

    @Override
    @TimeRange(TimePeriod.SHORT_TERM)
    public List<Track> getTopItems(TimePeriod timePeriod, int limit, int offset) {
        try {
            String url = String.format("%s/me/top/tracks?time_range=%s&limit=%d&offset=%d",
                SpotifyConstants.API_BASE_URL,
                timePeriod.getValue(),
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
