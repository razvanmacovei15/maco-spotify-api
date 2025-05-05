package com.maco.client.v2.interfaces;

import com.maco.client.v2.SpotifyToken;

@FunctionalInterface
public interface TokenUpdateListener {
    void onTokenUpdated(SpotifyToken newToken);
}
