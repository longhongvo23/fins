package com.stockapp.stockservice.domain;

import static com.stockapp.stockservice.domain.IntradayQuoteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.stockservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntradayQuoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntradayQuote.class);
        IntradayQuote intradayQuote1 = getIntradayQuoteSample1();
        IntradayQuote intradayQuote2 = new IntradayQuote();
        assertThat(intradayQuote1).isNotEqualTo(intradayQuote2);

        intradayQuote2.setId(intradayQuote1.getId());
        assertThat(intradayQuote1).isEqualTo(intradayQuote2);

        intradayQuote2 = getIntradayQuoteSample2();
        assertThat(intradayQuote1).isNotEqualTo(intradayQuote2);
    }
}
