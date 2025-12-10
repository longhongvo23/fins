package com.stockapp.userservice.service.mapper;

import static com.stockapp.userservice.domain.RefreshTokenAsserts.*;
import static com.stockapp.userservice.domain.RefreshTokenTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RefreshTokenMapperTest {

    private RefreshTokenMapper refreshTokenMapper;

    @BeforeEach
    void setUp() {
        refreshTokenMapper = new RefreshTokenMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRefreshTokenSample1();
        var actual = refreshTokenMapper.toEntity(refreshTokenMapper.toDto(expected));
        assertRefreshTokenAllPropertiesEquals(expected, actual);
    }
}
