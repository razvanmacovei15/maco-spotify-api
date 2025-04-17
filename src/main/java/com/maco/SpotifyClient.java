package com.maco;

import com.maco.auth.config.SpotifyConfig;
import com.maco.auth.callbackserver.AuthService;
import com.maco.auth.token.TokenManager;
import com.maco.service.SpotifyService;
import com.maco.utils.Browser;

public class SpotifyClient {
    private final SpotifyConfig clientConfig;
    private final TokenManager tokenManager;
    private final SpotifyService spotifyService;
    private boolean isAuthenticated = false;

    public SpotifyClient(SpotifyConfig clientConfig) {
        this.clientConfig = clientConfig;
        this.tokenManager = new TokenManager(clientConfig);
        this.spotifyService = new SpotifyService(tokenManager);
    }

    public void authenticate() {
        try {
            // 1. Create authorization URL
            String authUrl = clientConfig.createAuthorizationUrl();
            Browser.openUrl(authUrl);

            // 2. Wait for callback and get authorization code
            String code = AuthService.callbackServer();

            // 3. Exchange code for tokens
            tokenManager.authenticate(code);

            isAuthenticated = true;
            System.out.println("Successfully authenticated with Spotify!");

        } catch (Exception e) {
            throw new RuntimeException("Failed to authenticate with Spotify", e);
        }
    }
}
