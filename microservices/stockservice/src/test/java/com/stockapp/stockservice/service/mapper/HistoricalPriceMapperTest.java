package com.stockapp.stockservice.service.mapper;

import static com.stockapp.stockservice.domain.HistoricalPriceAsserts.*;
import static com.stockapp.stockservice.domain.HistoricalPriceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoricalPriceMapperTest {

    private HistoricalPriceMapper historicalPriceMapper;

    @BeforeEach
    void setUp() {
        historicalPriceMapper = new HistoricalPriceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHistoricalPriceSample1();
        var actual = historicalPriceMapper.toEntity(historicalPriceMapper.toDto(expected));
        assertHistoricalPriceAllPropertiesEquals(expected, actual);
    }
}
