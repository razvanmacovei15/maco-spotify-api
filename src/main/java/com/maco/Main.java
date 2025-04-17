package com.maco;

import com.maco.api.TimePeriod;
import com.maco.api.annotations.TimeRange;
import com.maco.auth.config.SpotifyConfig;
import com.maco.model.Track;

import java.io.IOException;
import java.lang.reflect.Field;
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
            List<Track> topTracks = client.getTopTracksLast4Weeks();
            
            // Print the top tracks
            System.out.println("\nYour Top Tracks from the last 4 weeks:");
            for (int i = 0; i < topTracks.size(); i++) {
                Track track = topTracks.get(i);
                System.out.printf("%d. %s%n", i + 1, track.getName());
            }
        } catch (Exception e) {
            System.err.println("Error getting top tracks: " + e.getMessage());
            e.printStackTrace();
        }
    }
}