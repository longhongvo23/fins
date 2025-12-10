package com.stockapp.notificationservice.domain;

import java.util.UUID;

public class NotificationTestSamples {

    public static Notification getNotificationSample1() {
        return new Notification().id("id1").recipient("recipient1").subject("subject1").errorMessage("errorMessage1");
    }

    public static Notification getNotificationSample2() {
        return new Notification().id("id2").recipient("recipient2").subject("subject2").errorMessage("errorMessage2");
    }

    public static Notification getNotificationRandomSampleGenerator() {
        return new Notification()
            .id(UUID.randomUUID().toString())
            .recipient(UUID.randomUUID().toString())
            .subject(UUID.randomUUID().toString())
            .errorMessage(UUID.randomUUID().toString());
    }
}
