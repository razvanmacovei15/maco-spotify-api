package com.maco.spotify.auth.callback;

import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class AuthCallbackService {

    public static String callbackServer() throws IOException, ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<String> futureResponse = new CompletableFuture<>();
        HttpServer server = HttpServer.create(new InetSocketAddress(8888), 0);

        server.createContext("/callback", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            log.info("Received callback with query: {}", query);

            String response = "Authentication successful!";
            exchange.sendResponseHeaders(200, response.length());

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            } catch (IOException e) {
                log.error("Error writing response to browser", e);
            }

            if (query != null && query.startsWith("code=")) {
                futureResponse.complete(query);
            } else {
                futureResponse.completeExceptionally(new IllegalArgumentException("Invalid callback query: " + query));
            }

            server.stop(0);
        });

        server.start();
        log.info("Callback server started. Waiting for callback...");

        try {
            String fullQuery = futureResponse.get(5, TimeUnit.MINUTES);
            return fullQuery.substring(5); // returns code after 'code='
        } finally {
            log.info("Shutting down callback server...");
            server.stop(0);
        }
    }
}
