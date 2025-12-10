package com.stockapp.newsservice.service.mapper;

import static com.stockapp.newsservice.domain.CompanyRefAsserts.*;
import static com.stockapp.newsservice.domain.CompanyRefTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompanyRefMapperTest {

    private CompanyRefMapper companyRefMapper;

    @BeforeEach
    void setUp() {
        companyRefMapper = new CompanyRefMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCompanyRefSample1();
        var actual = companyRefMapper.toEntity(companyRefMapper.toDto(expected));
        assertCompanyRefAllPropertiesEquals(expected, actual);
    }
}
