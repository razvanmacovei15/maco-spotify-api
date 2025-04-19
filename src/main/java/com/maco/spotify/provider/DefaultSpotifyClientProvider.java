package com.maco.spotify.provider;

import com.maco.spotify.api.client.SpotifyClient;
import com.maco.spotify.api.config.SpotifyConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DefaultSpotifyClientProvider implements SpotifyClientProvider{
    private final ConcurrentHashMap<String, SpotifyClient> clientConcurrentHashMap = new ConcurrentHashMap<>();
    private final SpotifyConfig config;
    private final ScheduledExecutorService cleanupExecutor = Executors.newSingleThreadScheduledExecutor();

    public DefaultSpotifyClientProvider(SpotifyConfig config) {
        this.config = config;
    }

    private void startCleanupTask(){
        cleanupExecutor.scheduleAtFixedRate(this::cleanUpInactiveClients, 1, 1, TimeUnit.HOURS);
    }

    private void cleanUpInactiveClients() {
        for(Map.Entry<String, SpotifyClient> entry : clientConcurrentHashMap.entrySet()){
            SpotifyClient client = entry.getValue();
            if(!client.isActive()){
                clientConcurrentHashMap.remove(entry.getKey());
            }
        }
    }

    @Override
    public SpotifyClient getClient(String userId) {
        return clientConcurrentHashMap.computeIfAbsent(userId, id -> {
            SpotifyClient client = new SpotifyClient(config);
            client.authenticate();
            return client;
        });
    }

    @Override
    public void removeClient(String userId) {
        clientConcurrentHashMap.remove(userId);
    }
}
