package com.stockapp.newsservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.newsservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompanyRefDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyRefDTO.class);
        CompanyRefDTO companyRefDTO1 = new CompanyRefDTO();
        companyRefDTO1.setId("id1");
        CompanyRefDTO companyRefDTO2 = new CompanyRefDTO();
        assertThat(companyRefDTO1).isNotEqualTo(companyRefDTO2);
        companyRefDTO2.setId(companyRefDTO1.getId());
        assertThat(companyRefDTO1).isEqualTo(companyRefDTO2);
        companyRefDTO2.setId("id2");
        assertThat(companyRefDTO1).isNotEqualTo(companyRefDTO2);
        companyRefDTO1.setId(null);
        assertThat(companyRefDTO1).isNotEqualTo(companyRefDTO2);
    }
}
