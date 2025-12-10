package com.stockapp.userservice.domain;

import static com.stockapp.userservice.domain.AppUserTestSamples.*;
import static com.stockapp.userservice.domain.PasswordHistoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.userservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PasswordHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PasswordHistory.class);
        PasswordHistory passwordHistory1 = getPasswordHistorySample1();
        PasswordHistory passwordHistory2 = new PasswordHistory();
        assertThat(passwordHistory1).isNotEqualTo(passwordHistory2);

        passwordHistory2.setId(passwordHistory1.getId());
        assertThat(passwordHistory1).isEqualTo(passwordHistory2);

        passwordHistory2 = getPasswordHistorySample2();
        assertThat(passwordHistory1).isNotEqualTo(passwordHistory2);
    }

    @Test
    void userTest() {
        PasswordHistory passwordHistory = getPasswordHistoryRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        passwordHistory.setUser(appUserBack);
        assertThat(passwordHistory.getUser()).isEqualTo(appUserBack);

        passwordHistory.user(null);
        assertThat(passwordHistory.getUser()).isNull();
    }
}
