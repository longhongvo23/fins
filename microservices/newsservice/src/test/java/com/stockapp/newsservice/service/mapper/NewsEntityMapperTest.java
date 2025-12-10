package com.stockapp.newsservice.service.mapper;

import static com.stockapp.newsservice.domain.NewsEntityAsserts.*;
import static com.stockapp.newsservice.domain.NewsEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NewsEntityMapperTest {

    private NewsEntityMapper newsEntityMapper;

    @BeforeEach
    void setUp() {
        newsEntityMapper = new NewsEntityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNewsEntitySample1();
        var actual = newsEntityMapper.toEntity(newsEntityMapper.toDto(expected));
        assertNewsEntityAllPropertiesEquals(expected, actual);
    }
}
