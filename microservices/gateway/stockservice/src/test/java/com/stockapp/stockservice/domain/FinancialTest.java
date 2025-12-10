package com.stockapp.stockservice.domain;

import static com.stockapp.stockservice.domain.CompanyTestSamples.*;
import static com.stockapp.stockservice.domain.FinancialTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.stockservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FinancialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Financial.class);
        Financial financial1 = getFinancialSample1();
        Financial financial2 = new Financial();
        assertThat(financial1).isNotEqualTo(financial2);

        financial2.setId(financial1.getId());
        assertThat(financial1).isEqualTo(financial2);

        financial2 = getFinancialSample2();
        assertThat(financial1).isNotEqualTo(financial2);
    }

    @Test
    void companyTest() {
        Financial financial = getFinancialRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        financial.setCompany(companyBack);
        assertThat(financial.getCompany()).isEqualTo(companyBack);

        financial.company(null);
        assertThat(financial.getCompany()).isNull();
    }
}
