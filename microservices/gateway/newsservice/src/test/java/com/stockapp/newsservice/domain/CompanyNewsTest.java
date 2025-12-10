package com.stockapp.newsservice.domain;

import static com.stockapp.newsservice.domain.CompanyNewsTestSamples.*;
import static com.stockapp.newsservice.domain.CompanyRefTestSamples.*;
import static com.stockapp.newsservice.domain.NewsEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.newsservice.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CompanyNewsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyNews.class);
        CompanyNews companyNews1 = getCompanyNewsSample1();
        CompanyNews companyNews2 = new CompanyNews();
        assertThat(companyNews1).isNotEqualTo(companyNews2);

        companyNews2.setId(companyNews1.getId());
        assertThat(companyNews1).isEqualTo(companyNews2);

        companyNews2 = getCompanyNewsSample2();
        assertThat(companyNews1).isNotEqualTo(companyNews2);
    }

    @Test
    void entitiesTest() {
        CompanyNews companyNews = getCompanyNewsRandomSampleGenerator();
        NewsEntity newsEntityBack = getNewsEntityRandomSampleGenerator();

        companyNews.addEntities(newsEntityBack);
        assertThat(companyNews.getEntities()).containsOnly(newsEntityBack);
        assertThat(newsEntityBack.getNews()).isEqualTo(companyNews);

        companyNews.removeEntities(newsEntityBack);
        assertThat(companyNews.getEntities()).doesNotContain(newsEntityBack);
        assertThat(newsEntityBack.getNews()).isNull();

        companyNews.entities(new HashSet<>(Set.of(newsEntityBack)));
        assertThat(companyNews.getEntities()).containsOnly(newsEntityBack);
        assertThat(newsEntityBack.getNews()).isEqualTo(companyNews);

        companyNews.setEntities(new HashSet<>());
        assertThat(companyNews.getEntities()).doesNotContain(newsEntityBack);
        assertThat(newsEntityBack.getNews()).isNull();
    }

    @Test
    void companyTest() {
        CompanyNews companyNews = getCompanyNewsRandomSampleGenerator();
        CompanyRef companyRefBack = getCompanyRefRandomSampleGenerator();

        companyNews.setCompany(companyRefBack);
        assertThat(companyNews.getCompany()).isEqualTo(companyRefBack);

        companyNews.company(null);
        assertThat(companyNews.getCompany()).isNull();
    }
}
