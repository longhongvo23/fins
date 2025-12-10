package com.stockapp.stockservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.stockservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PeerCompanyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeerCompanyDTO.class);
        PeerCompanyDTO peerCompanyDTO1 = new PeerCompanyDTO();
        peerCompanyDTO1.setId("id1");
        PeerCompanyDTO peerCompanyDTO2 = new PeerCompanyDTO();
        assertThat(peerCompanyDTO1).isNotEqualTo(peerCompanyDTO2);
        peerCompanyDTO2.setId(peerCompanyDTO1.getId());
        assertThat(peerCompanyDTO1).isEqualTo(peerCompanyDTO2);
        peerCompanyDTO2.setId("id2");
        assertThat(peerCompanyDTO1).isNotEqualTo(peerCompanyDTO2);
        peerCompanyDTO1.setId(null);
        assertThat(peerCompanyDTO1).isNotEqualTo(peerCompanyDTO2);
    }
}
