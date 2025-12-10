package com.stockapp.crawlservice.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuoteResponse {

    private String symbol;
    private String name;
    private String exchange;

    @JsonProperty("mic_code")
    private String micCode;

    private String currency;
    private String datetime;
    private Long timestamp;
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume;

    @JsonProperty("previous_close")
    private String previousClose;

    private String change;

    @JsonProperty("percent_change")
    private String percentChange;

    @JsonProperty("average_volume")
    private String averageVolume;

    @JsonProperty("is_market_open")
    private Boolean isMarketOpen;

    @JsonProperty("fifty_two_week")
    private FiftyTwoWeek fiftyTwoWeek;

    public static class FiftyTwoWeek {
        private String low;
        private String high;

        @JsonProperty("low_change")
        private String lowChange;

        @JsonProperty("high_change")
        private String highChange;

        @JsonProperty("low_change_percent")
        private String lowChangePercent;

        @JsonProperty("high_change_percent")
        private String highChangePercent;

        private String range;

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getLowChange() {
            return lowChange;
        }

        public void setLowChange(String lowChange) {
            this.lowChange = lowChange;
        }

        public String getHighChange() {
            return highChange;
        }

        public void setHighChange(String highChange) {
            this.highChange = highChange;
        }

        public String getLowChangePercent() {
            return lowChangePercent;
        }

        public void setLowChangePercent(String lowChangePercent) {
            this.lowChangePercent = lowChangePercent;
        }

        public String getHighChangePercent() {
            return highChangePercent;
        }

        public void setHighChangePercent(String highChangePercent) {
            this.highChangePercent = highChangePercent;
        }

        public String getRange() {
            return range;
        }

        public void setRange(String range) {
            this.range = range;
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getMicCode() {
        return micCode;
    }

    public void setMicCode(String micCode) {
        this.micCode = micCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(String previousClose) {
        this.previousClose = previousClose;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(String percentChange) {
        this.percentChange = percentChange;
    }

    public String getAverageVolume() {
        return averageVolume;
    }

    public void setAverageVolume(String averageVolume) {
        this.averageVolume = averageVolume;
    }

    public Boolean getIsMarketOpen() {
        return isMarketOpen;
    }

    public void setIsMarketOpen(Boolean isMarketOpen) {
        this.isMarketOpen = isMarketOpen;
    }

    public FiftyTwoWeek getFiftyTwoWeek() {
        return fiftyTwoWeek;
    }

    public void setFiftyTwoWeek(FiftyTwoWeek fiftyTwoWeek) {
        this.fiftyTwoWeek = fiftyTwoWeek;
    }
}
