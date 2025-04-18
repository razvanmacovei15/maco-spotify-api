package com.maco.spotify.utils;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Browser {

    public static void openUrl(String url) {
        if (!Desktop.isDesktopSupported()) {
            log.warn("Desktop is not supported on this platform.");
            printManualUrl(url);
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.BROWSE)) {
            log.warn("Desktop browsing is not supported on this platform.");
            printManualUrl(url);
            return;
        }

        try {
            desktop.browse(new URI(url));
            log.info("Successfully opened URL: {}", url);
        } catch (IOException | URISyntaxException e) {
            log.error("Failed to open browser for URL: {}", url, e);
            printManualUrl(url);
        }
    }

    private static void printManualUrl(String url) {
        System.out.println("Please visit this URL manually: " + url);
    }
}
