package com.stockapp.stockservice.domain;

import static com.stockapp.stockservice.domain.CompanyTestSamples.*;
import static com.stockapp.stockservice.domain.HistoricalPriceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.stockservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoricalPriceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoricalPrice.class);
        HistoricalPrice historicalPrice1 = getHistoricalPriceSample1();
        HistoricalPrice historicalPrice2 = new HistoricalPrice();
        assertThat(historicalPrice1).isNotEqualTo(historicalPrice2);

        historicalPrice2.setId(historicalPrice1.getId());
        assertThat(historicalPrice1).isEqualTo(historicalPrice2);

        historicalPrice2 = getHistoricalPriceSample2();
        assertThat(historicalPrice1).isNotEqualTo(historicalPrice2);
    }

    @Test
    void companyTest() {
        HistoricalPrice historicalPrice = getHistoricalPriceRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        historicalPrice.setCompany(companyBack);
        assertThat(historicalPrice.getCompany()).isEqualTo(companyBack);

        historicalPrice.company(null);
        assertThat(historicalPrice.getCompany()).isNull();
    }
}
