package com.maco.api.track;

import com.maco.api.TimePeriod;
import com.maco.api.annotations.TimeRange;
import com.maco.auth.token.TokenManager;
import com.maco.model.Track;
import com.maco.model.TracksResponse;
import com.maco.service.SpotifyService;
import com.maco.utils.SpotifyConstants;
import java.io.IOException;
import java.lang.reflect.Method;

public class TrackService extends SpotifyService {
    public TrackService(TokenManager tokenManager) {
        super(tokenManager);
    }

    public Track[] getTopTracks() {
        try {
            // Get the calling method
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            String callingClassName = stackTrace[3].getClassName();
            String callingMethodName = stackTrace[3].getMethodName();

            Class<?> callingClass = Class.forName(callingClassName);
            Method callingMethod = callingClass.getMethod(callingMethodName);

            // Get the annotation value
            TimeRange timeRange = callingMethod.getAnnotation(TimeRange.class);
            String timeRangeValue = timeRange != null ?
                    timeRange.value().getValue() :
                    TimePeriod.MEDIUM_TERM.getValue();

            System.out.println("Using time range: " + timeRangeValue);

            String url = SpotifyConstants.API_BASE_URL +
                    SpotifyConstants.USER_TOP_TRACKS_URL +
                    "?time_range=" + timeRangeValue;

            TracksResponse response = get(url, TracksResponse.class);
            return response.getItems();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to get top tracks", e);
        }
    }
}
