package com.stockapp.newsservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.newsservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NewsEntityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NewsEntityDTO.class);
        NewsEntityDTO newsEntityDTO1 = new NewsEntityDTO();
        newsEntityDTO1.setId("id1");
        NewsEntityDTO newsEntityDTO2 = new NewsEntityDTO();
        assertThat(newsEntityDTO1).isNotEqualTo(newsEntityDTO2);
        newsEntityDTO2.setId(newsEntityDTO1.getId());
        assertThat(newsEntityDTO1).isEqualTo(newsEntityDTO2);
        newsEntityDTO2.setId("id2");
        assertThat(newsEntityDTO1).isNotEqualTo(newsEntityDTO2);
        newsEntityDTO1.setId(null);
        assertThat(newsEntityDTO1).isNotEqualTo(newsEntityDTO2);
    }
}
