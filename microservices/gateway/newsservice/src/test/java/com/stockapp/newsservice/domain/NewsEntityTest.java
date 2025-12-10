package com.stockapp.newsservice.domain;

import static com.stockapp.newsservice.domain.CompanyNewsTestSamples.*;
import static com.stockapp.newsservice.domain.NewsEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.newsservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NewsEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NewsEntity.class);
        NewsEntity newsEntity1 = getNewsEntitySample1();
        NewsEntity newsEntity2 = new NewsEntity();
        assertThat(newsEntity1).isNotEqualTo(newsEntity2);

        newsEntity2.setId(newsEntity1.getId());
        assertThat(newsEntity1).isEqualTo(newsEntity2);

        newsEntity2 = getNewsEntitySample2();
        assertThat(newsEntity1).isNotEqualTo(newsEntity2);
    }

    @Test
    void newsTest() {
        NewsEntity newsEntity = getNewsEntityRandomSampleGenerator();
        CompanyNews companyNewsBack = getCompanyNewsRandomSampleGenerator();

        newsEntity.setNews(companyNewsBack);
        assertThat(newsEntity.getNews()).isEqualTo(companyNewsBack);

        newsEntity.news(null);
        assertThat(newsEntity.getNews()).isNull();
    }
}
