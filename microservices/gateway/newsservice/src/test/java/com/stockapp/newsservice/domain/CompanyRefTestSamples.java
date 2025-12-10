package com.stockapp.newsservice.domain;

import java.util.UUID;

public class CompanyRefTestSamples {

    public static CompanyRef getCompanyRefSample1() {
        return new CompanyRef().id("id1").symbol("symbol1").name("name1");
    }

    public static CompanyRef getCompanyRefSample2() {
        return new CompanyRef().id("id2").symbol("symbol2").name("name2");
    }

    public static CompanyRef getCompanyRefRandomSampleGenerator() {
        return new CompanyRef().id(UUID.randomUUID().toString()).symbol(UUID.randomUUID().toString()).name(UUID.randomUUID().toString());
    }
}
