package com.stockapp.stockservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class RecommendationTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Recommendation getRecommendationSample1() {
        return new Recommendation().id("id1").buy(1).hold(1).sell(1).strongBuy(1).strongSell(1);
    }

    public static Recommendation getRecommendationSample2() {
        return new Recommendation().id("id2").buy(2).hold(2).sell(2).strongBuy(2).strongSell(2);
    }

    public static Recommendation getRecommendationRandomSampleGenerator() {
        return new Recommendation()
            .id(UUID.randomUUID().toString())
            .buy(intCount.incrementAndGet())
            .hold(intCount.incrementAndGet())
            .sell(intCount.incrementAndGet())
            .strongBuy(intCount.incrementAndGet())
            .strongSell(intCount.incrementAndGet());
    }
}
