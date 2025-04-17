package com.maco;

import com.maco.api.TimePeriod;
import com.maco.api.annotations.TimeRange;
import com.maco.auth.config.SpotifyConfig;
import com.maco.model.Track;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        SpotifyConfig spotifyConfig = SpotifyConfig.fromProperties();

        // Create an instance of MyService
        MyService myService = new MyService(spotifyConfig);

        System.out.println("Getting recent tracks (short_term):");
        myService.getRecentTracks();
        System.out.println("\nGetting all-time tracks (long_term):");
        myService.getAllTimeTracks();
        System.out.println("\nGetting default tracks (medium_term):");
        myService.getDefaultTracks();
    }

    static class MyService {
        private final SpotifyClient client2;

        public MyService(SpotifyConfig spotifyConfig) throws IOException, ExecutionException, InterruptedException, TimeoutException {
            this.client2 = new SpotifyClient(spotifyConfig);
            this.client2.authenticate();
        }

        @TimeRange(TimePeriod.SHORT_TERM)
        public void getRecentTracks() {
            Track[] tracks = client2.getTopTracks();
            for (Track track : tracks) {
                System.out.println(track.getName());
            }
        }

        @TimeRange(TimePeriod.LONG_TERM)
        public void getAllTimeTracks() {
            Track[] tracks = client2.getTopTracks();
            for (Track track : tracks) {
                System.out.println(track.getName());
            }
        }

        public void getDefaultTracks() {
            Track[] tracks = client2.getTopTracks();
            for (Track track : tracks) {
                System.out.println(track.getName());
            }
        }
    }
}