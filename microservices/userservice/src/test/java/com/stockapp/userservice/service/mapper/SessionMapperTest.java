package com.stockapp.userservice.service.mapper;

import static com.stockapp.userservice.domain.SessionAsserts.*;
import static com.stockapp.userservice.domain.SessionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SessionMapperTest {

    private SessionMapper sessionMapper;

    @BeforeEach
    void setUp() {
        sessionMapper = new SessionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSessionSample1();
        var actual = sessionMapper.toEntity(sessionMapper.toDto(expected));
        assertSessionAllPropertiesEquals(expected, actual);
    }
}
