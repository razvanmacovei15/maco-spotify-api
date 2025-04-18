package com.maco.model.spotifyresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maco.model.Artist;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtistsResponse {
    @JsonProperty("items")
    private Artist[] items;

    @JsonProperty("total")
    private int total;

    @JsonProperty("limit")
    private int limit;

    @JsonProperty("offset")
    private int offset;
} 