package com.stockapp.stockservice.domain;

import java.util.UUID;

public class PeerCompanyTestSamples {

    public static PeerCompany getPeerCompanySample1() {
        return new PeerCompany().id("id1").peerSymbol("peerSymbol1");
    }

    public static PeerCompany getPeerCompanySample2() {
        return new PeerCompany().id("id2").peerSymbol("peerSymbol2");
    }

    public static PeerCompany getPeerCompanyRandomSampleGenerator() {
        return new PeerCompany().id(UUID.randomUUID().toString()).peerSymbol(UUID.randomUUID().toString());
    }
}
