package com.maco.api.interfaces;

import com.maco.api.TimePeriod;

import java.util.List;

public interface UserTopItems<T> {
    List<T> getTopItems(TimePeriod timePeriod, int limit, int offset);
}
