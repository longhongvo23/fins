package com.stockapp.userservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.userservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoginHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoginHistoryDTO.class);
        LoginHistoryDTO loginHistoryDTO1 = new LoginHistoryDTO();
        loginHistoryDTO1.setId("id1");
        LoginHistoryDTO loginHistoryDTO2 = new LoginHistoryDTO();
        assertThat(loginHistoryDTO1).isNotEqualTo(loginHistoryDTO2);
        loginHistoryDTO2.setId(loginHistoryDTO1.getId());
        assertThat(loginHistoryDTO1).isEqualTo(loginHistoryDTO2);
        loginHistoryDTO2.setId("id2");
        assertThat(loginHistoryDTO1).isNotEqualTo(loginHistoryDTO2);
        loginHistoryDTO1.setId(null);
        assertThat(loginHistoryDTO1).isNotEqualTo(loginHistoryDTO2);
    }
}
