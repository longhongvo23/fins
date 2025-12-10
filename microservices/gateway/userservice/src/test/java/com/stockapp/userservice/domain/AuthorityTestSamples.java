package com.stockapp.userservice.domain;

import java.util.UUID;

public class AuthorityTestSamples {

    public static Authority getAuthoritySample1() {
        return new Authority().id("id1").name("name1").description("description1").createdBy("createdBy1");
    }

    public static Authority getAuthoritySample2() {
        return new Authority().id("id2").name("name2").description("description2").createdBy("createdBy2");
    }

    public static Authority getAuthorityRandomSampleGenerator() {
        return new Authority()
            .id(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
