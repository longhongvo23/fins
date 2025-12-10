package com.stockapp.userservice.service.mapper;

import static com.stockapp.userservice.domain.PasswordHistoryAsserts.*;
import static com.stockapp.userservice.domain.PasswordHistoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PasswordHistoryMapperTest {

    private PasswordHistoryMapper passwordHistoryMapper;

    @BeforeEach
    void setUp() {
        passwordHistoryMapper = new PasswordHistoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPasswordHistorySample1();
        var actual = passwordHistoryMapper.toEntity(passwordHistoryMapper.toDto(expected));
        assertPasswordHistoryAllPropertiesEquals(expected, actual);
    }
}
