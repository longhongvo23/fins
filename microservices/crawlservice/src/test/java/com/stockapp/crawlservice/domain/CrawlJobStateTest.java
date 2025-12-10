package com.stockapp.crawlservice.domain;

import static com.stockapp.crawlservice.domain.CrawlJobStateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.crawlservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CrawlJobStateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrawlJobState.class);
        CrawlJobState crawlJobState1 = getCrawlJobStateSample1();
        CrawlJobState crawlJobState2 = new CrawlJobState();
        assertThat(crawlJobState1).isNotEqualTo(crawlJobState2);

        crawlJobState2.setId(crawlJobState1.getId());
        assertThat(crawlJobState1).isEqualTo(crawlJobState2);

        crawlJobState2 = getCrawlJobStateSample2();
        assertThat(crawlJobState1).isNotEqualTo(crawlJobState2);
    }
}
