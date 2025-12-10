package com.stockapp.newsservice.domain;

import java.util.UUID;

public class CompanyNewsTestSamples {

    public static CompanyNews getCompanyNewsSample1() {
        return new CompanyNews()
            .id("id1")
            .uuid("uuid1")
            .title("title1")
            .url("url1")
            .imageUrl("imageUrl1")
            .language("language1")
            .source("source1")
            .keywords("keywords1");
    }

    public static CompanyNews getCompanyNewsSample2() {
        return new CompanyNews()
            .id("id2")
            .uuid("uuid2")
            .title("title2")
            .url("url2")
            .imageUrl("imageUrl2")
            .language("language2")
            .source("source2")
            .keywords("keywords2");
    }

    public static CompanyNews getCompanyNewsRandomSampleGenerator() {
        return new CompanyNews()
            .id(UUID.randomUUID().toString())
            .uuid(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString())
            .imageUrl(UUID.randomUUID().toString())
            .language(UUID.randomUUID().toString())
            .source(UUID.randomUUID().toString())
            .keywords(UUID.randomUUID().toString());
    }
}
