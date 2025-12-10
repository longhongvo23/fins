package com.stockapp.newsservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.newsservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompanyNewsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyNewsDTO.class);
        CompanyNewsDTO companyNewsDTO1 = new CompanyNewsDTO();
        companyNewsDTO1.setId("id1");
        CompanyNewsDTO companyNewsDTO2 = new CompanyNewsDTO();
        assertThat(companyNewsDTO1).isNotEqualTo(companyNewsDTO2);
        companyNewsDTO2.setId(companyNewsDTO1.getId());
        assertThat(companyNewsDTO1).isEqualTo(companyNewsDTO2);
        companyNewsDTO2.setId("id2");
        assertThat(companyNewsDTO1).isNotEqualTo(companyNewsDTO2);
        companyNewsDTO1.setId(null);
        assertThat(companyNewsDTO1).isNotEqualTo(companyNewsDTO2);
    }
}
