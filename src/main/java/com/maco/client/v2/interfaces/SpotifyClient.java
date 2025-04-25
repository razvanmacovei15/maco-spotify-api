package com.maco.client.v2.interfaces;

import com.maco.client.v2.SpotifyToken;
import com.maco.client.v2.model.SpotifyArtist;
import com.maco.client.v2.model.SpotifyTrack;
import com.maco.client.v2.model.SpotifyUser;
import com.maco.client.v2.model.response.TokenResponse;

import java.util.List;

public interface SpotifyClient {
    boolean isAuthenticated();
    String getAuthorizationUrl(String state);
    void authenticate(String code);
    void deAuthenticate();
    SpotifyToken getAccessToken();
    void setAccessToken(String accessToken, String refreshToken, String tokenType, int expiresIn, String scope);
    SpotifyToken refreshAccessToken();
    SpotifyUser getCurrentUserDetails();
    List<SpotifyTrack> getTopTracksLast4Weeks(int limit, int offset);
    List<SpotifyTrack> getTopTracksLast6Months(int limit, int offset);
    List<SpotifyTrack> getTopTracksAllTime(int limit, int offset);
    List<SpotifyArtist> getTopArtistsLast4Weeks(int limit, int offset);
    List<SpotifyArtist> getTopArtistsLast6Months(int limit, int offset);
    List<SpotifyArtist> getTopArtistsAllTime(int limit, int offset);
}
