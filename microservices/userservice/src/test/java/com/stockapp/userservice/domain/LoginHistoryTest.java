package com.stockapp.userservice.domain;

import static com.stockapp.userservice.domain.AppUserTestSamples.*;
import static com.stockapp.userservice.domain.LoginHistoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.userservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoginHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoginHistory.class);
        LoginHistory loginHistory1 = getLoginHistorySample1();
        LoginHistory loginHistory2 = new LoginHistory();
        assertThat(loginHistory1).isNotEqualTo(loginHistory2);

        loginHistory2.setId(loginHistory1.getId());
        assertThat(loginHistory1).isEqualTo(loginHistory2);

        loginHistory2 = getLoginHistorySample2();
        assertThat(loginHistory1).isNotEqualTo(loginHistory2);
    }

    @Test
    void userTest() {
        LoginHistory loginHistory = getLoginHistoryRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        loginHistory.setUser(appUserBack);
        assertThat(loginHistory.getUser()).isEqualTo(appUserBack);

        loginHistory.user(null);
        assertThat(loginHistory.getUser()).isNull();
    }
}
