package com.stockapp.stockservice.domain;

import static com.stockapp.stockservice.domain.CompanyTestSamples.*;
import static com.stockapp.stockservice.domain.FinancialTestSamples.*;
import static com.stockapp.stockservice.domain.HistoricalPriceTestSamples.*;
import static com.stockapp.stockservice.domain.PeerCompanyTestSamples.*;
import static com.stockapp.stockservice.domain.RecommendationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.stockservice.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CompanyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Company.class);
        Company company1 = getCompanySample1();
        Company company2 = new Company();
        assertThat(company1).isNotEqualTo(company2);

        company2.setId(company1.getId());
        assertThat(company1).isEqualTo(company2);

        company2 = getCompanySample2();
        assertThat(company1).isNotEqualTo(company2);
    }

    @Test
    void historicalPricesTest() {
        Company company = getCompanyRandomSampleGenerator();
        HistoricalPrice historicalPriceBack = getHistoricalPriceRandomSampleGenerator();

        company.addHistoricalPrices(historicalPriceBack);
        assertThat(company.getHistoricalPrices()).containsOnly(historicalPriceBack);
        assertThat(historicalPriceBack.getCompany()).isEqualTo(company);

        company.removeHistoricalPrices(historicalPriceBack);
        assertThat(company.getHistoricalPrices()).doesNotContain(historicalPriceBack);
        assertThat(historicalPriceBack.getCompany()).isNull();

        company.historicalPrices(new HashSet<>(Set.of(historicalPriceBack)));
        assertThat(company.getHistoricalPrices()).containsOnly(historicalPriceBack);
        assertThat(historicalPriceBack.getCompany()).isEqualTo(company);

        company.setHistoricalPrices(new HashSet<>());
        assertThat(company.getHistoricalPrices()).doesNotContain(historicalPriceBack);
        assertThat(historicalPriceBack.getCompany()).isNull();
    }

    @Test
    void financialsTest() {
        Company company = getCompanyRandomSampleGenerator();
        Financial financialBack = getFinancialRandomSampleGenerator();

        company.addFinancials(financialBack);
        assertThat(company.getFinancials()).containsOnly(financialBack);
        assertThat(financialBack.getCompany()).isEqualTo(company);

        company.removeFinancials(financialBack);
        assertThat(company.getFinancials()).doesNotContain(financialBack);
        assertThat(financialBack.getCompany()).isNull();

        company.financials(new HashSet<>(Set.of(financialBack)));
        assertThat(company.getFinancials()).containsOnly(financialBack);
        assertThat(financialBack.getCompany()).isEqualTo(company);

        company.setFinancials(new HashSet<>());
        assertThat(company.getFinancials()).doesNotContain(financialBack);
        assertThat(financialBack.getCompany()).isNull();
    }

    @Test
    void recommendationsTest() {
        Company company = getCompanyRandomSampleGenerator();
        Recommendation recommendationBack = getRecommendationRandomSampleGenerator();

        company.addRecommendations(recommendationBack);
        assertThat(company.getRecommendations()).containsOnly(recommendationBack);
        assertThat(recommendationBack.getCompany()).isEqualTo(company);

        company.removeRecommendations(recommendationBack);
        assertThat(company.getRecommendations()).doesNotContain(recommendationBack);
        assertThat(recommendationBack.getCompany()).isNull();

        company.recommendations(new HashSet<>(Set.of(recommendationBack)));
        assertThat(company.getRecommendations()).containsOnly(recommendationBack);
        assertThat(recommendationBack.getCompany()).isEqualTo(company);

        company.setRecommendations(new HashSet<>());
        assertThat(company.getRecommendations()).doesNotContain(recommendationBack);
        assertThat(recommendationBack.getCompany()).isNull();
    }

    @Test
    void peersTest() {
        Company company = getCompanyRandomSampleGenerator();
        PeerCompany peerCompanyBack = getPeerCompanyRandomSampleGenerator();

        company.addPeers(peerCompanyBack);
        assertThat(company.getPeers()).containsOnly(peerCompanyBack);
        assertThat(peerCompanyBack.getCompany()).isEqualTo(company);

        company.removePeers(peerCompanyBack);
        assertThat(company.getPeers()).doesNotContain(peerCompanyBack);
        assertThat(peerCompanyBack.getCompany()).isNull();

        company.peers(new HashSet<>(Set.of(peerCompanyBack)));
        assertThat(company.getPeers()).containsOnly(peerCompanyBack);
        assertThat(peerCompanyBack.getCompany()).isEqualTo(company);

        company.setPeers(new HashSet<>());
        assertThat(company.getPeers()).doesNotContain(peerCompanyBack);
        assertThat(peerCompanyBack.getCompany()).isNull();
    }
}
