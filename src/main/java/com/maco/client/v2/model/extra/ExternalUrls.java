package com.maco.client.v2.model.extra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalUrls {
    @JsonProperty("spotify")
    private String spotify;
} 