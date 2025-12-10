package com.stockapp.userservice.domain;

import static com.stockapp.userservice.domain.AppUserTestSamples.*;
import static com.stockapp.userservice.domain.NotificationSettingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.userservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationSettingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationSetting.class);
        NotificationSetting notificationSetting1 = getNotificationSettingSample1();
        NotificationSetting notificationSetting2 = new NotificationSetting();
        assertThat(notificationSetting1).isNotEqualTo(notificationSetting2);

        notificationSetting2.setId(notificationSetting1.getId());
        assertThat(notificationSetting1).isEqualTo(notificationSetting2);

        notificationSetting2 = getNotificationSettingSample2();
        assertThat(notificationSetting1).isNotEqualTo(notificationSetting2);
    }

    @Test
    void userTest() {
        NotificationSetting notificationSetting = getNotificationSettingRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        notificationSetting.setUser(appUserBack);
        assertThat(notificationSetting.getUser()).isEqualTo(appUserBack);

        notificationSetting.user(null);
        assertThat(notificationSetting.getUser()).isNull();
    }
}
