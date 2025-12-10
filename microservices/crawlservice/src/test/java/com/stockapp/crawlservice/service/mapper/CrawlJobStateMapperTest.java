package com.stockapp.crawlservice.service.mapper;

import static com.stockapp.crawlservice.domain.CrawlJobStateAsserts.*;
import static com.stockapp.crawlservice.domain.CrawlJobStateTestSamples.*;

import com.stockapp.crawlservice.domain.CrawlJobState;
import com.stockapp.crawlservice.service.dto.CrawlJobStateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class CrawlJobStateMapperTest {

    private CrawlJobStateMapper crawlJobStateMapper;

    @BeforeEach
    void setUp() {
        crawlJobStateMapper = Mappers.getMapper(CrawlJobStateMapper.class);
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCrawlJobStateSample1();
        var actual = crawlJobStateMapper.toEntity(crawlJobStateMapper.toDto(expected));
        assertCrawlJobStateAllPropertiesEquals(expected, actual);
    }
}
