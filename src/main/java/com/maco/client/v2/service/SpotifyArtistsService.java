package com.maco.client.v2.service;

import com.maco.client.v2.enums.TimeRange;
import com.maco.client.v2.model.SpotifyArtist;
import com.maco.client.v2.model.response.ArtistSearchResponse;
import com.maco.client.v2.model.response.ArtistsResponse;
import com.maco.client.v2.model.response.SearchWrapper;
import com.maco.client.v2.utils.SpotifyConstants;
import com.maco.client.v2.utils.SpotifyHttpClient;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service class for accessing Spotify's top artists data for a user.
 * Extends {@link AbstractSpotifyService} to utilize shared Spotify API logic.
 */
public class SpotifyArtistsService extends AbstractSpotifyService {

    /**
     * Constructs the SpotifyArtistsService with required dependencies.
     *
     * @param spotifyHttpClient the HTTP client for performing requests
     * @param clientId          Spotify application client ID
     * @param clientSecret      Spotify application client secret
     * @param headers           initial headers to use for requests
     */
    public SpotifyArtistsService(SpotifyHttpClient spotifyHttpClient, String clientId, String clientSecret, Map<String, String> headers) {
        super(spotifyHttpClient, clientId, clientSecret, headers);
    }

    /**
     * Retrieves the current user's top artists for a specified time range.
     *
     * @param timeRange the Spotify time range (e.g., short_term, medium_term, long_term)
     * @param limit     the maximum number of items to return
     * @param offset    the index of the first item to return (for pagination)
     * @return a list of {@link SpotifyArtist} representing the user's top artists
     * @throws RuntimeException if the API call fails or the response cannot be parsed
     */
    public List<SpotifyArtist> getTopItems(TimeRange timeRange, int limit, int offset) {
        try {
            String url = String.format("%s%s?time_range=%s&limit=%d&offset=%d",
                    SpotifyConstants.API_BASE_URL,
                    SpotifyConstants.USER_TOP_ARTISTS_URL,
                    timeRange.getValue(),
                    limit,
                    offset
            );

            ArtistsResponse response = get(url, ArtistsResponse.class, headers);
            return Arrays.asList(response.getItems());
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch top artists", e);
        }
    }

    /**
     * Searches for artists based on the provided name.
     *
     * @param artistName the name of the artist to search for
     * @param type       the type of item to search for (e.g., artist)
     * @param limit      the maximum number of items to return
     * @param offset     the index of the first item to return (for pagination)
     * @return a list of {@link SpotifyArtist} matching the search criteria
     * @throws RuntimeException if the API call fails or the response cannot be parsed
     */
    public List<SpotifyArtist> searchForArtist(String artistName, String type, int limit, int offset) {
        try {
            String encodedArtistName = URLEncoder.encode(artistName, StandardCharsets.UTF_8);
            String url = String.format("%s%s?q=%s&type=%s&limit=%d&offset=%d",
                    SpotifyConstants.API_BASE_URL,
                    SpotifyConstants.SEARCH_URL,
                    encodedArtistName,
                    type,
                    limit,
                    offset
            );

            SearchWrapper response = get(url, SearchWrapper.class, headers);
            return Arrays.asList(response.getArtists().getItems());
        } catch (IOException e) {
            throw new RuntimeException("Failed to search for artists", e);
        }
    }
}
