package com.stockapp.newsservice.domain;

import java.util.UUID;

public class NewsEntityTestSamples {

    public static NewsEntity getNewsEntitySample1() {
        return new NewsEntity()
            .id("id1")
            .newsUuid("newsUuid1")
            .symbol("symbol1")
            .name("name1")
            .exchange("exchange1")
            .country("country1")
            .type("type1")
            .industry("industry1");
    }

    public static NewsEntity getNewsEntitySample2() {
        return new NewsEntity()
            .id("id2")
            .newsUuid("newsUuid2")
            .symbol("symbol2")
            .name("name2")
            .exchange("exchange2")
            .country("country2")
            .type("type2")
            .industry("industry2");
    }

    public static NewsEntity getNewsEntityRandomSampleGenerator() {
        return new NewsEntity()
            .id(UUID.randomUUID().toString())
            .newsUuid(UUID.randomUUID().toString())
            .symbol(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .exchange(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .industry(UUID.randomUUID().toString());
    }
}
