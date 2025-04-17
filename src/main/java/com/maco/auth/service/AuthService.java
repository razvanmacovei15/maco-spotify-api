package com.maco.auth.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AuthService {
    public static String callbackServer() throws IOException, ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<String> futureResponse  = new CompletableFuture<>();

        HttpServer server = HttpServer.create(new InetSocketAddress(8888), 0);
        server.createContext("/callback", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();
                String response = "Authentication successful!";

                exchange.sendResponseHeaders(200, response.length());
                try(OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
                futureResponse.complete(query);
                server.stop(0);
            }
        });
        server.start();
        System.out.println("Waiting for callback...");
        try {
            return futureResponse.get(5, TimeUnit.MINUTES).substring(5);
        } finally {
            server.stop(0);
        }
    }
}
