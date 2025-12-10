package com.stockapp.stockservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StockStatisticsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StockStatistics getStockStatisticsSample1() {
        return new StockStatistics()
            .id("id1")
            .symbol("symbol1")
            .averageVolume(1L)
            .fiftyTwoWeekRange("fiftyTwoWeekRange1")
            .extendedTimestamp(1L);
    }

    public static StockStatistics getStockStatisticsSample2() {
        return new StockStatistics()
            .id("id2")
            .symbol("symbol2")
            .averageVolume(2L)
            .fiftyTwoWeekRange("fiftyTwoWeekRange2")
            .extendedTimestamp(2L);
    }

    public static StockStatistics getStockStatisticsRandomSampleGenerator() {
        return new StockStatistics()
            .id(UUID.randomUUID().toString())
            .symbol(UUID.randomUUID().toString())
            .averageVolume(longCount.incrementAndGet())
            .fiftyTwoWeekRange(UUID.randomUUID().toString())
            .extendedTimestamp(longCount.incrementAndGet());
    }
}
