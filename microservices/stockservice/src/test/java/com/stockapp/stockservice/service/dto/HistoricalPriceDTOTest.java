package com.stockapp.stockservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.stockservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoricalPriceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoricalPriceDTO.class);
        HistoricalPriceDTO historicalPriceDTO1 = new HistoricalPriceDTO();
        historicalPriceDTO1.setId("id1");
        HistoricalPriceDTO historicalPriceDTO2 = new HistoricalPriceDTO();
        assertThat(historicalPriceDTO1).isNotEqualTo(historicalPriceDTO2);
        historicalPriceDTO2.setId(historicalPriceDTO1.getId());
        assertThat(historicalPriceDTO1).isEqualTo(historicalPriceDTO2);
        historicalPriceDTO2.setId("id2");
        assertThat(historicalPriceDTO1).isNotEqualTo(historicalPriceDTO2);
        historicalPriceDTO1.setId(null);
        assertThat(historicalPriceDTO1).isNotEqualTo(historicalPriceDTO2);
    }
}
