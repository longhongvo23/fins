package com.stockapp.userservice.domain;

import java.util.UUID;

public class SessionTestSamples {

    public static Session getSessionSample1() {
        return new Session()
            .id("id1")
            .sessionId("sessionId1")
            .token("token1")
            .refreshToken("refreshToken1")
            .ipAddress("ipAddress1")
            .userAgent("userAgent1")
            .deviceId("deviceId1")
            .deviceName("deviceName1")
            .osName("osName1")
            .osVersion("osVersion1")
            .browserName("browserName1")
            .browserVersion("browserVersion1")
            .location("location1")
            .revokedReason("revokedReason1");
    }

    public static Session getSessionSample2() {
        return new Session()
            .id("id2")
            .sessionId("sessionId2")
            .token("token2")
            .refreshToken("refreshToken2")
            .ipAddress("ipAddress2")
            .userAgent("userAgent2")
            .deviceId("deviceId2")
            .deviceName("deviceName2")
            .osName("osName2")
            .osVersion("osVersion2")
            .browserName("browserName2")
            .browserVersion("browserVersion2")
            .location("location2")
            .revokedReason("revokedReason2");
    }

    public static Session getSessionRandomSampleGenerator() {
        return new Session()
            .id(UUID.randomUUID().toString())
            .sessionId(UUID.randomUUID().toString())
            .token(UUID.randomUUID().toString())
            .refreshToken(UUID.randomUUID().toString())
            .ipAddress(UUID.randomUUID().toString())
            .userAgent(UUID.randomUUID().toString())
            .deviceId(UUID.randomUUID().toString())
            .deviceName(UUID.randomUUID().toString())
            .osName(UUID.randomUUID().toString())
            .osVersion(UUID.randomUUID().toString())
            .browserName(UUID.randomUUID().toString())
            .browserVersion(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .revokedReason(UUID.randomUUID().toString());
    }
}
