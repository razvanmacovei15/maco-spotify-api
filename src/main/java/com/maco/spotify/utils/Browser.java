package com.maco.spotify.utils;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class Browser {

    public static void openUrl(String url) {
        if (!Desktop.isDesktopSupported()) {
            printManualUrl(url);
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.BROWSE)) {
            printManualUrl(url);
            return;
        }

        try {
            desktop.browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            printManualUrl(url);
        }
    }

    private static void printManualUrl(String url) {
        System.out.println("Please visit this URL manually: " + url);
    }
}
