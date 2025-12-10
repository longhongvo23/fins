package com.stockapp.crawlservice.service.mapper;

import static com.stockapp.crawlservice.domain.CrawlJobStateAsserts.*;
import static com.stockapp.crawlservice.domain.CrawlJobStateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CrawlJobStateMapperTest {

    private CrawlJobStateMapper crawlJobStateMapper;

    @BeforeEach
    void setUp() {
        crawlJobStateMapper = new CrawlJobStateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCrawlJobStateSample1();
        var actual = crawlJobStateMapper.toEntity(crawlJobStateMapper.toDto(expected));
        assertCrawlJobStateAllPropertiesEquals(expected, actual);
    }
}
