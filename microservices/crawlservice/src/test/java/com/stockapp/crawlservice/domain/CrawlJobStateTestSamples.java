package com.stockapp.crawlservice.domain;

import java.util.UUID;

public class CrawlJobStateTestSamples {

    public static CrawlJobState getCrawlJobStateSample1() {
        return new CrawlJobState().id("id1").symbol("symbol1");
    }

    public static CrawlJobState getCrawlJobStateSample2() {
        return new CrawlJobState().id("id2").symbol("symbol2");
    }

    public static CrawlJobState getCrawlJobStateRandomSampleGenerator() {
        return new CrawlJobState().id(UUID.randomUUID().toString()).symbol(UUID.randomUUID().toString());
    }
}
