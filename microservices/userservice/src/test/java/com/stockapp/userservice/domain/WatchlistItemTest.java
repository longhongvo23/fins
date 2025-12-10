package com.stockapp.userservice.domain;

import static com.stockapp.userservice.domain.AppUserTestSamples.*;
import static com.stockapp.userservice.domain.WatchlistItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.userservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WatchlistItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WatchlistItem.class);
        WatchlistItem watchlistItem1 = getWatchlistItemSample1();
        WatchlistItem watchlistItem2 = new WatchlistItem();
        assertThat(watchlistItem1).isNotEqualTo(watchlistItem2);

        watchlistItem2.setId(watchlistItem1.getId());
        assertThat(watchlistItem1).isEqualTo(watchlistItem2);

        watchlistItem2 = getWatchlistItemSample2();
        assertThat(watchlistItem1).isNotEqualTo(watchlistItem2);
    }

    @Test
    void userTest() {
        WatchlistItem watchlistItem = getWatchlistItemRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        watchlistItem.setUser(appUserBack);
        assertThat(watchlistItem.getUser()).isEqualTo(appUserBack);

        watchlistItem.user(null);
        assertThat(watchlistItem.getUser()).isNull();
    }
}
