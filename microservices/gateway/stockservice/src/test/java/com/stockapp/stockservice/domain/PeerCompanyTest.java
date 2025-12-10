package com.stockapp.stockservice.domain;

import static com.stockapp.stockservice.domain.CompanyTestSamples.*;
import static com.stockapp.stockservice.domain.PeerCompanyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.stockservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PeerCompanyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeerCompany.class);
        PeerCompany peerCompany1 = getPeerCompanySample1();
        PeerCompany peerCompany2 = new PeerCompany();
        assertThat(peerCompany1).isNotEqualTo(peerCompany2);

        peerCompany2.setId(peerCompany1.getId());
        assertThat(peerCompany1).isEqualTo(peerCompany2);

        peerCompany2 = getPeerCompanySample2();
        assertThat(peerCompany1).isNotEqualTo(peerCompany2);
    }

    @Test
    void companyTest() {
        PeerCompany peerCompany = getPeerCompanyRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        peerCompany.setCompany(companyBack);
        assertThat(peerCompany.getCompany()).isEqualTo(companyBack);

        peerCompany.company(null);
        assertThat(peerCompany.getCompany()).isNull();
    }
}
