package com.stockapp.stockservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.stockservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockStatisticsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockStatisticsDTO.class);
        StockStatisticsDTO stockStatisticsDTO1 = new StockStatisticsDTO();
        stockStatisticsDTO1.setId("id1");
        StockStatisticsDTO stockStatisticsDTO2 = new StockStatisticsDTO();
        assertThat(stockStatisticsDTO1).isNotEqualTo(stockStatisticsDTO2);
        stockStatisticsDTO2.setId(stockStatisticsDTO1.getId());
        assertThat(stockStatisticsDTO1).isEqualTo(stockStatisticsDTO2);
        stockStatisticsDTO2.setId("id2");
        assertThat(stockStatisticsDTO1).isNotEqualTo(stockStatisticsDTO2);
        stockStatisticsDTO1.setId(null);
        assertThat(stockStatisticsDTO1).isNotEqualTo(stockStatisticsDTO2);
    }
}
