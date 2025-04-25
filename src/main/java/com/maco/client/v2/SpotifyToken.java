package com.maco.client.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpotifyToken {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long expiresIn;
    private String scope;
    private Instant createdAt;

}
