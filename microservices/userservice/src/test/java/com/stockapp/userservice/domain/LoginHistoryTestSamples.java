package com.stockapp.userservice.domain;

import java.util.UUID;

public class LoginHistoryTestSamples {

    public static LoginHistory getLoginHistorySample1() {
        return new LoginHistory()
            .id("id1")
            .ipAddress("ipAddress1")
            .userAgent("userAgent1")
            .location("location1")
            .failureReason("failureReason1");
    }

    public static LoginHistory getLoginHistorySample2() {
        return new LoginHistory()
            .id("id2")
            .ipAddress("ipAddress2")
            .userAgent("userAgent2")
            .location("location2")
            .failureReason("failureReason2");
    }

    public static LoginHistory getLoginHistoryRandomSampleGenerator() {
        return new LoginHistory()
            .id(UUID.randomUUID().toString())
            .ipAddress(UUID.randomUUID().toString())
            .userAgent(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .failureReason(UUID.randomUUID().toString());
    }
}
