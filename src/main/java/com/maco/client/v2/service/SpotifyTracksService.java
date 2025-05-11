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

/**
 * Service class for accessing Spotify's top tracks data for a user.
 * Inherits shared logic from {@link AbstractSpotifyService}.
 */
public class SpotifyTracksService extends AbstractSpotifyService {

    /**
     * Constructs the SpotifyTracksService with required dependencies.
     *
     * @param spotifyHttpClient the HTTP client used for Spotify API communication
     * @param clientId          Spotify application client ID
     * @param clientSecret      Spotify application client secret
     * @param headers           initial headers to use for Spotify API requests
     */
    public SpotifyTracksService(SpotifyHttpClient spotifyHttpClient, String clientId, String clientSecret, Map<String, String> headers) {
        super(spotifyHttpClient, clientId, clientSecret, headers);
    }

    /**
     * Retrieves the current user's top tracks for a given time range.
     *
     * @param timeRange the Spotify time range (e.g., short_term, medium_term, long_term)
     * @param limit     the number of tracks to return
     * @param offset    the starting point for pagination
     * @return a list of {@link SpotifyTrack} representing the user's top tracks
     * @throws RuntimeException if the API call or response parsing fails
     */
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
