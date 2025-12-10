package com.stockapp.userservice.domain;

import static com.stockapp.userservice.domain.AppUserTestSamples.*;
import static com.stockapp.userservice.domain.AuthorityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.userservice.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AuthorityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Authority.class);
        Authority authority1 = getAuthoritySample1();
        Authority authority2 = new Authority();
        assertThat(authority1).isNotEqualTo(authority2);

        authority2.setId(authority1.getId());
        assertThat(authority1).isEqualTo(authority2);

        authority2 = getAuthoritySample2();
        assertThat(authority1).isNotEqualTo(authority2);
    }

    @Test
    void usersTest() {
        Authority authority = getAuthorityRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        authority.addUsers(appUserBack);
        assertThat(authority.getUsers()).containsOnly(appUserBack);
        assertThat(appUserBack.getAuthorities()).containsOnly(authority);

        authority.removeUsers(appUserBack);
        assertThat(authority.getUsers()).doesNotContain(appUserBack);
        assertThat(appUserBack.getAuthorities()).doesNotContain(authority);

        authority.users(new HashSet<>(Set.of(appUserBack)));
        assertThat(authority.getUsers()).containsOnly(appUserBack);
        assertThat(appUserBack.getAuthorities()).containsOnly(authority);

        authority.setUsers(new HashSet<>());
        assertThat(authority.getUsers()).doesNotContain(appUserBack);
        assertThat(appUserBack.getAuthorities()).doesNotContain(authority);
    }
}
