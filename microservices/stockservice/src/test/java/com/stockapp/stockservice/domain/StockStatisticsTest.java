package com.stockapp.stockservice.domain;

import static com.stockapp.stockservice.domain.StockStatisticsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.stockservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockStatisticsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockStatistics.class);
        StockStatistics stockStatistics1 = getStockStatisticsSample1();
        StockStatistics stockStatistics2 = new StockStatistics();
        assertThat(stockStatistics1).isNotEqualTo(stockStatistics2);

        stockStatistics2.setId(stockStatistics1.getId());
        assertThat(stockStatistics1).isEqualTo(stockStatistics2);

        stockStatistics2 = getStockStatisticsSample2();
        assertThat(stockStatistics1).isNotEqualTo(stockStatistics2);
    }
}
