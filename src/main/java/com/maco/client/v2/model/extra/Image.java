package com.maco.client.v2.model.extra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {
    @JsonProperty("url")
    private String url;

    @JsonProperty("height")
    private int height;

    @JsonProperty("width")
    private int width;
} 