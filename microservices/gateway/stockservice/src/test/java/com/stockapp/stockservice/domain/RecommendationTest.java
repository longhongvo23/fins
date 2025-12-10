package com.stockapp.stockservice.domain;

import static com.stockapp.stockservice.domain.CompanyTestSamples.*;
import static com.stockapp.stockservice.domain.RecommendationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.stockservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecommendationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recommendation.class);
        Recommendation recommendation1 = getRecommendationSample1();
        Recommendation recommendation2 = new Recommendation();
        assertThat(recommendation1).isNotEqualTo(recommendation2);

        recommendation2.setId(recommendation1.getId());
        assertThat(recommendation1).isEqualTo(recommendation2);

        recommendation2 = getRecommendationSample2();
        assertThat(recommendation1).isNotEqualTo(recommendation2);
    }

    @Test
    void companyTest() {
        Recommendation recommendation = getRecommendationRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        recommendation.setCompany(companyBack);
        assertThat(recommendation.getCompany()).isEqualTo(companyBack);

        recommendation.company(null);
        assertThat(recommendation.getCompany()).isNull();
    }
}
