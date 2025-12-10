package com.stockapp.newsservice.domain;

import static com.stockapp.newsservice.domain.CompanyNewsTestSamples.*;
import static com.stockapp.newsservice.domain.CompanyRefTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.newsservice.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CompanyRefTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyRef.class);
        CompanyRef companyRef1 = getCompanyRefSample1();
        CompanyRef companyRef2 = new CompanyRef();
        assertThat(companyRef1).isNotEqualTo(companyRef2);

        companyRef2.setId(companyRef1.getId());
        assertThat(companyRef1).isEqualTo(companyRef2);

        companyRef2 = getCompanyRefSample2();
        assertThat(companyRef1).isNotEqualTo(companyRef2);
    }

    @Test
    void companyNewsTest() {
        CompanyRef companyRef = getCompanyRefRandomSampleGenerator();
        CompanyNews companyNewsBack = getCompanyNewsRandomSampleGenerator();

        companyRef.addCompanyNews(companyNewsBack);
        assertThat(companyRef.getCompanyNews()).containsOnly(companyNewsBack);
        assertThat(companyNewsBack.getCompany()).isEqualTo(companyRef);

        companyRef.removeCompanyNews(companyNewsBack);
        assertThat(companyRef.getCompanyNews()).doesNotContain(companyNewsBack);
        assertThat(companyNewsBack.getCompany()).isNull();

        companyRef.companyNews(new HashSet<>(Set.of(companyNewsBack)));
        assertThat(companyRef.getCompanyNews()).containsOnly(companyNewsBack);
        assertThat(companyNewsBack.getCompany()).isEqualTo(companyRef);

        companyRef.setCompanyNews(new HashSet<>());
        assertThat(companyRef.getCompanyNews()).doesNotContain(companyNewsBack);
        assertThat(companyNewsBack.getCompany()).isNull();
    }
}
