package com.stockapp.userservice.domain;

import java.util.UUID;

public class NotificationSettingTestSamples {

    public static NotificationSetting getNotificationSettingSample1() {
        return new NotificationSetting().id("id1");
    }

    public static NotificationSetting getNotificationSettingSample2() {
        return new NotificationSetting().id("id2");
    }

    public static NotificationSetting getNotificationSettingRandomSampleGenerator() {
        return new NotificationSetting().id(UUID.randomUUID().toString());
    }
}
