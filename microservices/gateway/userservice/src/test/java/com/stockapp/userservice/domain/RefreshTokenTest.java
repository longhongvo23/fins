package com.stockapp.userservice.domain;

import static com.stockapp.userservice.domain.AppUserTestSamples.*;
import static com.stockapp.userservice.domain.RefreshTokenTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.userservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RefreshTokenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RefreshToken.class);
        RefreshToken refreshToken1 = getRefreshTokenSample1();
        RefreshToken refreshToken2 = new RefreshToken();
        assertThat(refreshToken1).isNotEqualTo(refreshToken2);

        refreshToken2.setId(refreshToken1.getId());
        assertThat(refreshToken1).isEqualTo(refreshToken2);

        refreshToken2 = getRefreshTokenSample2();
        assertThat(refreshToken1).isNotEqualTo(refreshToken2);
    }

    @Test
    void userTest() {
        RefreshToken refreshToken = getRefreshTokenRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        refreshToken.setUser(appUserBack);
        assertThat(refreshToken.getUser()).isEqualTo(appUserBack);

        refreshToken.user(null);
        assertThat(refreshToken.getUser()).isNull();
    }
}
