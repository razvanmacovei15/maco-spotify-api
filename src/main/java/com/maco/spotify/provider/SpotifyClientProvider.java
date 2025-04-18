package com.maco.spotify.provider;

import com.maco.spotify.api.client.SpotifyClient;

public interface SpotifyClientProvider {
    SpotifyClient getClient();
    void removeClient();
}
