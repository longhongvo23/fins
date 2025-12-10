package com.stockapp.stockservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.stockservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FinancialDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinancialDTO.class);
        FinancialDTO financialDTO1 = new FinancialDTO();
        financialDTO1.setId("id1");
        FinancialDTO financialDTO2 = new FinancialDTO();
        assertThat(financialDTO1).isNotEqualTo(financialDTO2);
        financialDTO2.setId(financialDTO1.getId());
        assertThat(financialDTO1).isEqualTo(financialDTO2);
        financialDTO2.setId("id2");
        assertThat(financialDTO1).isNotEqualTo(financialDTO2);
        financialDTO1.setId(null);
        assertThat(financialDTO1).isNotEqualTo(financialDTO2);
    }
}
