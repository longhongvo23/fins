package com.stockapp.stockservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.stockservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntradayQuoteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntradayQuoteDTO.class);
        IntradayQuoteDTO intradayQuoteDTO1 = new IntradayQuoteDTO();
        intradayQuoteDTO1.setId("id1");
        IntradayQuoteDTO intradayQuoteDTO2 = new IntradayQuoteDTO();
        assertThat(intradayQuoteDTO1).isNotEqualTo(intradayQuoteDTO2);
        intradayQuoteDTO2.setId(intradayQuoteDTO1.getId());
        assertThat(intradayQuoteDTO1).isEqualTo(intradayQuoteDTO2);
        intradayQuoteDTO2.setId("id2");
        assertThat(intradayQuoteDTO1).isNotEqualTo(intradayQuoteDTO2);
        intradayQuoteDTO1.setId(null);
        assertThat(intradayQuoteDTO1).isNotEqualTo(intradayQuoteDTO2);
    }
}
