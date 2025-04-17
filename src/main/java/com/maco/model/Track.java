package com.maco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

}