package com.maco.api;

import lombok.Getter;

@Getter
public enum TimePeriod {
    SHORT_TERM("short_term"),
    MEDIUM_TERM("medium_term"),
    LONG_TERM("long_term");

    private final String value;

    TimePeriod(String value) {
        this.value = value;
    }

}