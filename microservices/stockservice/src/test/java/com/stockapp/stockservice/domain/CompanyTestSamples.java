package com.stockapp.stockservice.domain;

import java.util.UUID;

public class CompanyTestSamples {

    public static Company getCompanySample1() {
        return new Company()
            .id("id1")
            .symbol("symbol1")
            .name("name1")
            .country("country1")
            .currency("currency1")
            .exchange("exchange1")
            .finnhubIndustry("finnhubIndustry1")
            .logo("logo1")
            .weburl("weburl1")
            .phone("phone1");
    }

    public static Company getCompanySample2() {
        return new Company()
            .id("id2")
            .symbol("symbol2")
            .name("name2")
            .country("country2")
            .currency("currency2")
            .exchange("exchange2")
            .finnhubIndustry("finnhubIndustry2")
            .logo("logo2")
            .weburl("weburl2")
            .phone("phone2");
    }

    public static Company getCompanyRandomSampleGenerator() {
        return new Company()
            .id(UUID.randomUUID().toString())
            .symbol(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString())
            .exchange(UUID.randomUUID().toString())
            .finnhubIndustry(UUID.randomUUID().toString())
            .logo(UUID.randomUUID().toString())
            .weburl(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString());
    }
}
