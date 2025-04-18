package com.maco.spotify.api.service;

import com.maco.spotify.api.enums.TimeRange;

import java.util.List;

public interface UserTopItems<T> {
    List<T> getTopItems(TimeRange tr, int limit, int offset);
}
