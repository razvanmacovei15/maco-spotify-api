package com.maco.client.v2.model.response;

import lombok.Getter;

@Getter
public class SearchWrapper {
    private ArtistSearchResponse artists;

    public void setArtists(ArtistSearchResponse artists) {
        this.artists = artists;
    }
}
