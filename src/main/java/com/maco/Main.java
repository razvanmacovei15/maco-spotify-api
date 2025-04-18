package com.maco;

import com.maco.auth.config.SpotifyConfig;
import com.maco.model.Track;
import com.maco.model.Artist;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        // Load Spotify configuration from properties file
        SpotifyConfig spotifyConfig = SpotifyConfig.fromProperties();
        
        // Create Spotify client
        SpotifyClient client = new SpotifyClient(spotifyConfig);
        
        // Authenticate with Spotify
        client.authenticate();
        
        try {
            // Get top tracks from the last 4 weeks
            List<Track> topTracks = client.getTopTracksLast4Weeks(50, 0);
            
            // Print the top tracks
            System.out.println("\nYour Top Tracks from the last 4 weeks:");
            for (int i = 0; i < topTracks.size(); i++) {
                Track track = topTracks.get(i);
                System.out.printf("%d. %s - %s%n", i + 1, track.getName(), track.getArtists()[0].getName());
            }

            // Get top tracks from the last 6 months
            List<Track> topTracksMedium = client.getTopTracksLast6Months(50, 0);

            // Print the top tracks
            System.out.println("\nYour Top Tracks from the last 6 months:");
            for (int i = 0; i < topTracksMedium.size(); i++) {
                Track track = topTracksMedium.get(i);
                System.out.printf("%d. %s - %s%n", i + 1, track.getName(), track.getArtists()[0].getName());
            }

            // Get top artists from the last 4 weeks
            List<Artist> topArtists = client.getTopArtistsLast4Weeks(50, 0);
            
            // Print the top artists
            System.out.println("\nYour Top Artists from the last 4 weeks:");
            for (int i = 0; i < topArtists.size(); i++) {
                Artist artist = topArtists.get(i);
                System.out.printf("%d. %s%n", i + 1, artist.getName());
            }

            // Get top artists from the last 6 months
            List<Artist> topArtistsMedium = client.getTopArtistsLast6Months(50, 0);
            
            // Print the top artists
            System.out.println("\nYour Top Artists from the last 6 months:");
            for (int i = 0; i < topArtistsMedium.size(); i++) {
                Artist artist = topArtistsMedium.get(i);
                System.out.printf("%d. %s%n", i + 1, artist.getName());
            }

            // Get top artists of all time
            List<Artist> topArtistsAllTime = client.getTopArtistsAllTime(50, 0);
            
            // Print the top artists
            System.out.println("\nYour Top Artists of All Time:");
            for (int i = 0; i < topArtistsAllTime.size(); i++) {
                Artist artist = topArtistsAllTime.get(i);
                System.out.printf("%d. %s%n", i + 1, artist.getName());
            }

        } catch (Exception e) {
            System.err.println("Error getting top items: " + e.getMessage());
            e.printStackTrace();
        }
    }
}