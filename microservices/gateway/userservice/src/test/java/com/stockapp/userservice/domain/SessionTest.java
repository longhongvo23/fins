package com.stockapp.userservice.domain;

import static com.stockapp.userservice.domain.AppUserTestSamples.*;
import static com.stockapp.userservice.domain.SessionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.userservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SessionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Session.class);
        Session session1 = getSessionSample1();
        Session session2 = new Session();
        assertThat(session1).isNotEqualTo(session2);

        session2.setId(session1.getId());
        assertThat(session1).isEqualTo(session2);

        session2 = getSessionSample2();
        assertThat(session1).isNotEqualTo(session2);
    }

    @Test
    void userTest() {
        Session session = getSessionRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        session.setUser(appUserBack);
        assertThat(session.getUser()).isEqualTo(appUserBack);

        session.user(null);
        assertThat(session.getUser()).isNull();
    }
}
