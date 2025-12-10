package com.stockapp.stockservice.service.mapper;

import static com.stockapp.stockservice.domain.FinancialAsserts.*;
import static com.stockapp.stockservice.domain.FinancialTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FinancialMapperTest {

    private FinancialMapper financialMapper;

    @BeforeEach
    void setUp() {
        financialMapper = new FinancialMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFinancialSample1();
        var actual = financialMapper.toEntity(financialMapper.toDto(expected));
        assertFinancialAllPropertiesEquals(expected, actual);
    }
}
