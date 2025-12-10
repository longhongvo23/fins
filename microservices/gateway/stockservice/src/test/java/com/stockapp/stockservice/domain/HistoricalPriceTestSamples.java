package com.stockapp.stockservice.domain;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HistoricalPriceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HistoricalPrice getHistoricalPriceSample1() {
        return new HistoricalPrice()
                .id("id1")
                .symbol("symbol1")
                .datetime(Instant.parse("2020-01-01T00:00:00Z"))
                .interval("interval1")
                .volume(1L);
    }

    public static HistoricalPrice getHistoricalPriceSample2() {
        return new HistoricalPrice()
                .id("id2")
                .symbol("symbol2")
                .datetime(Instant.parse("2021-01-01T00:00:00Z"))
                .interval("interval2")
                .volume(2L);
    }

    public static HistoricalPrice getHistoricalPriceRandomSampleGenerator() {
        return new HistoricalPrice()
                .id(UUID.randomUUID().toString())
                .symbol(UUID.randomUUID().toString())
                .datetime(Instant.ofEpochMilli(Math.abs(random.nextLong() % 1_000_000L)))
                .interval(UUID.randomUUID().toString())
                .volume(longCount.incrementAndGet());
    }
}
