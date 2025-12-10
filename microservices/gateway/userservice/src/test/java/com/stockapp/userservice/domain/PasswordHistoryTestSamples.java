package com.stockapp.userservice.domain;

import java.util.UUID;

public class PasswordHistoryTestSamples {

    public static PasswordHistory getPasswordHistorySample1() {
        return new PasswordHistory().id("id1").passwordHash("passwordHash1").changedBy("changedBy1").changeReason("changeReason1");
    }

    public static PasswordHistory getPasswordHistorySample2() {
        return new PasswordHistory().id("id2").passwordHash("passwordHash2").changedBy("changedBy2").changeReason("changeReason2");
    }

    public static PasswordHistory getPasswordHistoryRandomSampleGenerator() {
        return new PasswordHistory()
            .id(UUID.randomUUID().toString())
            .passwordHash(UUID.randomUUID().toString())
            .changedBy(UUID.randomUUID().toString())
            .changeReason(UUID.randomUUID().toString());
    }
}
