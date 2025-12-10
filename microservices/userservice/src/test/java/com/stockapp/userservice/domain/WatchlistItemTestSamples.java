package com.stockapp.userservice.domain;

import java.util.UUID;

public class WatchlistItemTestSamples {

    public static WatchlistItem getWatchlistItemSample1() {
        return new WatchlistItem().id("id1").symbol("symbol1").notes("notes1");
    }

    public static WatchlistItem getWatchlistItemSample2() {
        return new WatchlistItem().id("id2").symbol("symbol2").notes("notes2");
    }

    public static WatchlistItem getWatchlistItemRandomSampleGenerator() {
        return new WatchlistItem()
            .id(UUID.randomUUID().toString())
            .symbol(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
