package com.maco.client.v2.interfaces;

import com.maco.client.v2.SpotifyToken;

/**
 * Functional interface used to listen for Spotify token updates.
 * Implementations can be notified when a new {@link SpotifyToken} is issued or refreshed.
 */
@FunctionalInterface
public interface TokenUpdateListener {

    /**
     * Called when a new token has been issued or an existing token has been refreshed.
     *
     * @param newToken the newly issued {@link SpotifyToken}
     */
    void onTokenUpdated(SpotifyToken newToken);
}
