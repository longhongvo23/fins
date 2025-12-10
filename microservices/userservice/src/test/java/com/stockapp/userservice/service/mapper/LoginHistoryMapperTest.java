package com.stockapp.userservice.service.mapper;

import static com.stockapp.userservice.domain.LoginHistoryAsserts.*;
import static com.stockapp.userservice.domain.LoginHistoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginHistoryMapperTest {

    private LoginHistoryMapper loginHistoryMapper;

    @BeforeEach
    void setUp() {
        loginHistoryMapper = new LoginHistoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLoginHistorySample1();
        var actual = loginHistoryMapper.toEntity(loginHistoryMapper.toDto(expected));
        assertLoginHistoryAllPropertiesEquals(expected, actual);
    }
}
