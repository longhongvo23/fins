package com.stockapp.stockservice.service.mapper;

import static com.stockapp.stockservice.domain.StockStatisticsAsserts.*;
import static com.stockapp.stockservice.domain.StockStatisticsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StockStatisticsMapperTest {

    private StockStatisticsMapper stockStatisticsMapper;

    @BeforeEach
    void setUp() {
        stockStatisticsMapper = new StockStatisticsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStockStatisticsSample1();
        var actual = stockStatisticsMapper.toEntity(stockStatisticsMapper.toDto(expected));
        assertStockStatisticsAllPropertiesEquals(expected, actual);
    }
}
