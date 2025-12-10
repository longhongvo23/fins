package com.stockapp.stockservice.domain;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HistoricalPriceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    private static final Instant SAMPLE_INSTANT_1 = Instant.parse("2020-01-01T00:00:00Z");
    private static final Instant SAMPLE_INSTANT_2 = Instant.parse("2020-01-02T00:00:00Z");

    public static HistoricalPrice getHistoricalPriceSample1() {
        return new HistoricalPrice().id("id1").symbol("symbol1").datetime(SAMPLE_INSTANT_1).interval("interval1")
                .volume(1L);
    }

    public static HistoricalPrice getHistoricalPriceSample2() {
        return new HistoricalPrice().id("id2").symbol("symbol2").datetime(SAMPLE_INSTANT_2).interval("interval2")
                .volume(2L);
    }

    public static HistoricalPrice getHistoricalPriceRandomSampleGenerator() {
        return new HistoricalPrice()
                .id(UUID.randomUUID().toString())
                .symbol(UUID.randomUUID().toString())
                .datetime(Instant.now())
                .interval(UUID.randomUUID().toString())
                .volume(longCount.incrementAndGet());
    }
}
