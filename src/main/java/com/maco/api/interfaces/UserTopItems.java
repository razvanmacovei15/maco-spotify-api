package com.maco.api.interfaces;

import com.maco.api.TimeRange;

import java.util.List;

public interface UserTopItems<T> {
    List<T> getTopItems(TimeRange tr, int limit, int offset);
}
