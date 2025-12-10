package com.stockapp.stockservice.service.mapper;

import static com.stockapp.stockservice.domain.PeerCompanyAsserts.*;
import static com.stockapp.stockservice.domain.PeerCompanyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PeerCompanyMapperTest {

    private PeerCompanyMapper peerCompanyMapper;

    @BeforeEach
    void setUp() {
        peerCompanyMapper = new PeerCompanyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPeerCompanySample1();
        var actual = peerCompanyMapper.toEntity(peerCompanyMapper.toDto(expected));
        assertPeerCompanyAllPropertiesEquals(expected, actual);
    }
}
