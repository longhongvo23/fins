package com.stockapp.stockservice.web.rest.vm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * View Model for trending stocks (gainers/losers)
 */
public class TrendingStockVM implements Serializable {

    private String symbol;
    private String name;
    private BigDecimal currentPrice;
    private BigDecimal previousClose;
    private BigDecimal change;
    private BigDecimal changePercent;
    private Long volume;

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

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(BigDecimal previousClose) {
        this.previousClose = previousClose;
    }

    public BigDecimal getChange() {
        return change;
    }

    public void setChange(BigDecimal change) {
        this.change = change;
    }

    public BigDecimal getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(BigDecimal changePercent) {
        this.changePercent = changePercent;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    /**
     * Calculate change and change percent from current and previous prices
     */
    public void calculateChange() {
        if (currentPrice != null && previousClose != null && previousClose.compareTo(BigDecimal.ZERO) > 0) {
            this.change = currentPrice.subtract(previousClose);
            this.changePercent = change
                    .divide(previousClose, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            this.change = BigDecimal.ZERO;
            this.changePercent = BigDecimal.ZERO;
        }
    }

    @Override
    public String toString() {
        return "TrendingStockVM{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", currentPrice=" + currentPrice +
                ", change=" + change +
                ", changePercent=" + changePercent +
                ", volume=" + volume +
                '}';
    }
}
