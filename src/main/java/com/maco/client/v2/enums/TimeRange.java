package com.maco.client.v2.enums;

import lombok.Getter;

/**
 * Enum representing the time ranges supported by the Spotify Web API
 * for fetching a user's top tracks or artists.
 */
@Getter
public enum TimeRange {

    /**
     * Approximately last 4 weeks of data.
     */
    SHORT_TERM("short_term"),

    /**
     * Approximately last 6 months of data.
     */
    MEDIUM_TERM("medium_term"),

    /**
     * All-time data (several years of listening history).
     */
    LONG_TERM("long_term");

    /**
     * The raw value expected by the Spotify Web API.
     */
    private final String value;

    TimeRange(String value) {
        this.value = value;
    }
}
