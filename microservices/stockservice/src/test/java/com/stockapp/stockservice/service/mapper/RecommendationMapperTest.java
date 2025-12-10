package com.stockapp.stockservice.service.mapper;

import static com.stockapp.stockservice.domain.RecommendationAsserts.*;
import static com.stockapp.stockservice.domain.RecommendationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecommendationMapperTest {

    private RecommendationMapper recommendationMapper;

    @BeforeEach
    void setUp() {
        recommendationMapper = new RecommendationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRecommendationSample1();
        var actual = recommendationMapper.toEntity(recommendationMapper.toDto(expected));
        assertRecommendationAllPropertiesEquals(expected, actual);
    }
}
