package com.stockapp.stockservice.service.mapper;

import static com.stockapp.stockservice.domain.IntradayQuoteAsserts.*;
import static com.stockapp.stockservice.domain.IntradayQuoteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntradayQuoteMapperTest {

    private IntradayQuoteMapper intradayQuoteMapper;

    @BeforeEach
    void setUp() {
        intradayQuoteMapper = new IntradayQuoteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIntradayQuoteSample1();
        var actual = intradayQuoteMapper.toEntity(intradayQuoteMapper.toDto(expected));
        assertIntradayQuoteAllPropertiesEquals(expected, actual);
    }
}
