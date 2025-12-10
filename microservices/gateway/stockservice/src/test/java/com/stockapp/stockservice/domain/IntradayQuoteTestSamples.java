package com.stockapp.stockservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IntradayQuoteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IntradayQuote getIntradayQuoteSample1() {
        return new IntradayQuote()
            .id("id1")
            .symbol("symbol1")
            .name("name1")
            .exchange("exchange1")
            .micCode("micCode1")
            .currency("currency1")
            .datetime("datetime1")
            .timestamp(1L)
            .lastQuoteAt(1L)
            .volume(1L)
            .averageVolume(1L);
    }

    public static IntradayQuote getIntradayQuoteSample2() {
        return new IntradayQuote()
            .id("id2")
            .symbol("symbol2")
            .name("name2")
            .exchange("exchange2")
            .micCode("micCode2")
            .currency("currency2")
            .datetime("datetime2")
            .timestamp(2L)
            .lastQuoteAt(2L)
            .volume(2L)
            .averageVolume(2L);
    }

    public static IntradayQuote getIntradayQuoteRandomSampleGenerator() {
        return new IntradayQuote()
            .id(UUID.randomUUID().toString())
            .symbol(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .exchange(UUID.randomUUID().toString())
            .micCode(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString())
            .datetime(UUID.randomUUID().toString())
            .timestamp(longCount.incrementAndGet())
            .lastQuoteAt(longCount.incrementAndGet())
            .volume(longCount.incrementAndGet())
            .averageVolume(longCount.incrementAndGet());
    }
}
