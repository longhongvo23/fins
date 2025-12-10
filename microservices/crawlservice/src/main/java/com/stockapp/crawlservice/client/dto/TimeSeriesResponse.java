package com.stockapp.crawlservice.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TimeSeriesResponse {

    private Meta meta;
    private List<Value> values;
    private String status;

    public static class Meta {
        private String symbol;
        private String interval;
        private String currency;

        @JsonProperty("exchange_timezone")
        private String exchangeTimezone;

        @JsonProperty("exchange")
        private String exchange;

        @JsonProperty("mic_code")
        private String micCode;

        @JsonProperty("type")
        private String type;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getInterval() {
            return interval;
        }

        public void setInterval(String interval) {
            this.interval = interval;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getExchangeTimezone() {
            return exchangeTimezone;
        }

        public void setExchangeTimezone(String exchangeTimezone) {
            this.exchangeTimezone = exchangeTimezone;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class Value {
        private String datetime;
        private String open;
        private String high;
        private String low;
        private String close;
        private String volume;

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
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
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
