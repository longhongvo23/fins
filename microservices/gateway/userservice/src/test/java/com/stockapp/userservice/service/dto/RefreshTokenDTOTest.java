package com.stockapp.userservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.userservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RefreshTokenDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RefreshTokenDTO.class);
        RefreshTokenDTO refreshTokenDTO1 = new RefreshTokenDTO();
        refreshTokenDTO1.setId("id1");
        RefreshTokenDTO refreshTokenDTO2 = new RefreshTokenDTO();
        assertThat(refreshTokenDTO1).isNotEqualTo(refreshTokenDTO2);
        refreshTokenDTO2.setId(refreshTokenDTO1.getId());
        assertThat(refreshTokenDTO1).isEqualTo(refreshTokenDTO2);
        refreshTokenDTO2.setId("id2");
        assertThat(refreshTokenDTO1).isNotEqualTo(refreshTokenDTO2);
        refreshTokenDTO1.setId(null);
        assertThat(refreshTokenDTO1).isNotEqualTo(refreshTokenDTO2);
    }
}
