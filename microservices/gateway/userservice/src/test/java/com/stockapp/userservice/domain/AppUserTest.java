package com.stockapp.userservice.domain;

import static com.stockapp.userservice.domain.AppUserTestSamples.*;
import static com.stockapp.userservice.domain.AuthorityTestSamples.*;
import static com.stockapp.userservice.domain.NotificationSettingTestSamples.*;
import static com.stockapp.userservice.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.userservice.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AppUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppUser.class);
        AppUser appUser1 = getAppUserSample1();
        AppUser appUser2 = new AppUser();
        assertThat(appUser1).isNotEqualTo(appUser2);

        appUser2.setId(appUser1.getId());
        assertThat(appUser1).isEqualTo(appUser2);

        appUser2 = getAppUserSample2();
        assertThat(appUser1).isNotEqualTo(appUser2);
    }

    @Test
    void authoritiesTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        Authority authorityBack = getAuthorityRandomSampleGenerator();

        appUser.addAuthorities(authorityBack);
        assertThat(appUser.getAuthorities()).containsOnly(authorityBack);

        appUser.removeAuthorities(authorityBack);
        assertThat(appUser.getAuthorities()).doesNotContain(authorityBack);

        appUser.authorities(new HashSet<>(Set.of(authorityBack)));
        assertThat(appUser.getAuthorities()).containsOnly(authorityBack);

        appUser.setAuthorities(new HashSet<>());
        assertThat(appUser.getAuthorities()).doesNotContain(authorityBack);
    }

    @Test
    void profileTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        appUser.setProfile(userProfileBack);
        assertThat(appUser.getProfile()).isEqualTo(userProfileBack);
        assertThat(userProfileBack.getUser()).isEqualTo(appUser);

        appUser.profile(null);
        assertThat(appUser.getProfile()).isNull();
        assertThat(userProfileBack.getUser()).isNull();
    }

    @Test
    void notificationSettingTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        NotificationSetting notificationSettingBack = getNotificationSettingRandomSampleGenerator();

        appUser.setNotificationSetting(notificationSettingBack);
        assertThat(appUser.getNotificationSetting()).isEqualTo(notificationSettingBack);
        assertThat(notificationSettingBack.getUser()).isEqualTo(appUser);

        appUser.notificationSetting(null);
        assertThat(appUser.getNotificationSetting()).isNull();
        assertThat(notificationSettingBack.getUser()).isNull();
    }
}
