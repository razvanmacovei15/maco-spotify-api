package com.maco.spotify.utils;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Browser {
    public static void openUrl(String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(url));
                } else {
                    System.err.println("Desktop browsing is not supported on this platform");
                    System.out.println("Please visit this URL manually: " + url);
                }
            } else {
                System.err.println("Desktop is not supported on this platform");
                System.out.println("Please visit this URL manually: " + url);
            }
        } catch (IOException | URISyntaxException e) {
            System.err.println("Failed to open browser: " + e.getMessage());
            System.out.println("Please visit this URL manually: " + url);
        }
    }
}