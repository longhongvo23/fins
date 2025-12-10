package com.stockapp.crawlservice.client.dto;

import java.util.List;

public class RecommendationResponse {

    private String symbol;
    private String period;
    private Integer buy;
    private Integer hold;
    private Integer sell;
    private Integer strongBuy;
    private Integer strongSell;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Integer getBuy() {
        return buy;
    }

    public void setBuy(Integer buy) {
        this.buy = buy;
    }

    public Integer getHold() {
        return hold;
    }

    public void setHold(Integer hold) {
        this.hold = hold;
    }

    public Integer getSell() {
        return sell;
    }

    public void setSell(Integer sell) {
        this.sell = sell;
    }

    public Integer getStrongBuy() {
        return strongBuy;
    }

    public void setStrongBuy(Integer strongBuy) {
        this.strongBuy = strongBuy;
    }

    public Integer getStrongSell() {
        return strongSell;
    }

    public void setStrongSell(Integer strongSell) {
        this.strongSell = strongSell;
    }
}
