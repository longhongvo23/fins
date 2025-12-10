package com.stockapp.userservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.userservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WatchlistItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WatchlistItemDTO.class);
        WatchlistItemDTO watchlistItemDTO1 = new WatchlistItemDTO();
        watchlistItemDTO1.setId("id1");
        WatchlistItemDTO watchlistItemDTO2 = new WatchlistItemDTO();
        assertThat(watchlistItemDTO1).isNotEqualTo(watchlistItemDTO2);
        watchlistItemDTO2.setId(watchlistItemDTO1.getId());
        assertThat(watchlistItemDTO1).isEqualTo(watchlistItemDTO2);
        watchlistItemDTO2.setId("id2");
        assertThat(watchlistItemDTO1).isNotEqualTo(watchlistItemDTO2);
        watchlistItemDTO1.setId(null);
        assertThat(watchlistItemDTO1).isNotEqualTo(watchlistItemDTO2);
    }
}
