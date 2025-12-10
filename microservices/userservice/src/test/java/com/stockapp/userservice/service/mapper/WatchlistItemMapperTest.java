package com.stockapp.userservice.service.mapper;

import static com.stockapp.userservice.domain.WatchlistItemAsserts.*;
import static com.stockapp.userservice.domain.WatchlistItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WatchlistItemMapperTest {

    private WatchlistItemMapper watchlistItemMapper;

    @BeforeEach
    void setUp() {
        watchlistItemMapper = new WatchlistItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWatchlistItemSample1();
        var actual = watchlistItemMapper.toEntity(watchlistItemMapper.toDto(expected));
        assertWatchlistItemAllPropertiesEquals(expected, actual);
    }
}
