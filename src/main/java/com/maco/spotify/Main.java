package com.maco.spotify;

import com.maco.spotify.api.client.SpotifyClient;
import com.maco.spotify.api.config.SpotifyConfig;
import com.maco.spotify.api.model.SpotifyArtist;
import com.maco.spotify.api.model.SpotifyTrack;
import com.maco.spotify.api.model.SpotifyUser;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        SpotifyConfig config = SpotifyConfig.fromProperties("spotify.properties");
        SpotifyClient client = new SpotifyClient(config);

        client.authenticate();

        // Get top tracks from the last 4 weeks
        List<SpotifyTrack> topTracks = client.getTopTracksLast4Weeks(45, 0);

        // Print the top tracks
        System.out.println("\nYour Top Tracks from the last 4 weeks:");
        for (int i = 0; i < topTracks.size(); i++) {
            SpotifyTrack track = topTracks.get(i);
            System.out.printf("%d. %s - %s%n", i + 1, track.getName(), track.getSpotifyArtists()[0].getName());
        }
        List<SpotifyArtist> topArtistsAllTime = client.getTopArtistsAllTime(45, 0);

        // Print the top artists
        System.out.println("\nYour Top Artists of All Time:");
        for (int i = 0; i < topArtistsAllTime.size(); i++) {
            SpotifyArtist artist = topArtistsAllTime.get(i);
            System.out.printf("%d. %s%n", i + 1, artist.getName());
        }

        SpotifyUser user = client.getUserDetails();
        System.out.println(user.toString());
    }
}