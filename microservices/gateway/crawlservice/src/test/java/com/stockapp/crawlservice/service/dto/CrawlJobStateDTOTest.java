package com.stockapp.crawlservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.crawlservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CrawlJobStateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrawlJobStateDTO.class);
        CrawlJobStateDTO crawlJobStateDTO1 = new CrawlJobStateDTO();
        crawlJobStateDTO1.setId("id1");
        CrawlJobStateDTO crawlJobStateDTO2 = new CrawlJobStateDTO();
        assertThat(crawlJobStateDTO1).isNotEqualTo(crawlJobStateDTO2);
        crawlJobStateDTO2.setId(crawlJobStateDTO1.getId());
        assertThat(crawlJobStateDTO1).isEqualTo(crawlJobStateDTO2);
        crawlJobStateDTO2.setId("id2");
        assertThat(crawlJobStateDTO1).isNotEqualTo(crawlJobStateDTO2);
        crawlJobStateDTO1.setId(null);
        assertThat(crawlJobStateDTO1).isNotEqualTo(crawlJobStateDTO2);
    }
}
