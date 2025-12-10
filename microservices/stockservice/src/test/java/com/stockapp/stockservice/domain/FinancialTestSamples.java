package com.stockapp.stockservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class FinancialTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Financial getFinancialSample1() {
        return new Financial().id("id1").symbol("symbol1").year(1);
    }

    public static Financial getFinancialSample2() {
        return new Financial().id("id2").symbol("symbol2").year(2);
    }

    public static Financial getFinancialRandomSampleGenerator() {
        return new Financial().id(UUID.randomUUID().toString()).symbol(UUID.randomUUID().toString()).year(intCount.incrementAndGet());
    }
}
