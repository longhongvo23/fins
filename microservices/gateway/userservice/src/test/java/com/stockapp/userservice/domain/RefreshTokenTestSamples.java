package com.stockapp.userservice.domain;

import java.util.UUID;

public class RefreshTokenTestSamples {

    public static RefreshToken getRefreshTokenSample1() {
        return new RefreshToken()
            .id("id1")
            .token("token1")
            .ipAddress("ipAddress1")
            .userAgent("userAgent1")
            .deviceId("deviceId1")
            .revokedReason("revokedReason1")
            .replacedByToken("replacedByToken1");
    }

    public static RefreshToken getRefreshTokenSample2() {
        return new RefreshToken()
            .id("id2")
            .token("token2")
            .ipAddress("ipAddress2")
            .userAgent("userAgent2")
            .deviceId("deviceId2")
            .revokedReason("revokedReason2")
            .replacedByToken("replacedByToken2");
    }

    public static RefreshToken getRefreshTokenRandomSampleGenerator() {
        return new RefreshToken()
            .id(UUID.randomUUID().toString())
            .token(UUID.randomUUID().toString())
            .ipAddress(UUID.randomUUID().toString())
            .userAgent(UUID.randomUUID().toString())
            .deviceId(UUID.randomUUID().toString())
            .revokedReason(UUID.randomUUID().toString())
            .replacedByToken(UUID.randomUUID().toString());
    }
}
