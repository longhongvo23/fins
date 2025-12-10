package com.stockapp.userservice.domain;

import java.util.UUID;

public class UserProfileTestSamples {

    public static UserProfile getUserProfileSample1() {
        return new UserProfile()
            .id("id1")
            .phoneNumber("phoneNumber1")
            .country("country1")
            .fullName("fullName1")
            .avatarUrl("avatarUrl1")
            .bio("bio1")
            .profileVisibility("profileVisibility1");
    }

    public static UserProfile getUserProfileSample2() {
        return new UserProfile()
            .id("id2")
            .phoneNumber("phoneNumber2")
            .country("country2")
            .fullName("fullName2")
            .avatarUrl("avatarUrl2")
            .bio("bio2")
            .profileVisibility("profileVisibility2");
    }

    public static UserProfile getUserProfileRandomSampleGenerator() {
        return new UserProfile()
            .id(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .fullName(UUID.randomUUID().toString())
            .avatarUrl(UUID.randomUUID().toString())
            .bio(UUID.randomUUID().toString())
            .profileVisibility(UUID.randomUUID().toString());
    }
}
