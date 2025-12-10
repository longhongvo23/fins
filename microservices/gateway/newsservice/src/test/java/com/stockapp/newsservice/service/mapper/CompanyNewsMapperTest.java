package com.stockapp.newsservice.service.mapper;

import static com.stockapp.newsservice.domain.CompanyNewsAsserts.*;
import static com.stockapp.newsservice.domain.CompanyNewsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompanyNewsMapperTest {

    private CompanyNewsMapper companyNewsMapper;

    @BeforeEach
    void setUp() {
        companyNewsMapper = new CompanyNewsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCompanyNewsSample1();
        var actual = companyNewsMapper.toEntity(companyNewsMapper.toDto(expected));
        assertCompanyNewsAllPropertiesEquals(expected, actual);
    }
}
