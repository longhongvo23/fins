package com.stockapp.userservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.userservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PasswordHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PasswordHistoryDTO.class);
        PasswordHistoryDTO passwordHistoryDTO1 = new PasswordHistoryDTO();
        passwordHistoryDTO1.setId("id1");
        PasswordHistoryDTO passwordHistoryDTO2 = new PasswordHistoryDTO();
        assertThat(passwordHistoryDTO1).isNotEqualTo(passwordHistoryDTO2);
        passwordHistoryDTO2.setId(passwordHistoryDTO1.getId());
        assertThat(passwordHistoryDTO1).isEqualTo(passwordHistoryDTO2);
        passwordHistoryDTO2.setId("id2");
        assertThat(passwordHistoryDTO1).isNotEqualTo(passwordHistoryDTO2);
        passwordHistoryDTO1.setId(null);
        assertThat(passwordHistoryDTO1).isNotEqualTo(passwordHistoryDTO2);
    }
}
