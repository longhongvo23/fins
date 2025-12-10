package com.stockapp.crawlservice.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfileResponse {

    private String country;
    private String currency;
    private String exchange;
    private String ipo;

    @JsonProperty("marketCapitalization")
    private Double marketCapitalization;

    private String name;
    private String phone;

    @JsonProperty("shareOutstanding")
    private Double shareOutstanding;

    private String ticker;
    private String weburl;
    private String logo;

    @JsonProperty("finnhubIndustry")
    private String finnhubIndustry;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getIpo() {
        return ipo;
    }

    public void setIpo(String ipo) {
        this.ipo = ipo;
    }

    public Double getMarketCapitalization() {
        return marketCapitalization;
    }

    public void setMarketCapitalization(Double marketCapitalization) {
        this.marketCapitalization = marketCapitalization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getShareOutstanding() {
        return shareOutstanding;
    }

    public void setShareOutstanding(Double shareOutstanding) {
        this.shareOutstanding = shareOutstanding;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFinnhubIndustry() {
        return finnhubIndustry;
    }

    public void setFinnhubIndustry(String finnhubIndustry) {
        this.finnhubIndustry = finnhubIndustry;
    }
}
