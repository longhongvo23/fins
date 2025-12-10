package com.stockapp.userservice.web.rest.vm;

import java.io.Serializable;
import java.time.Instant;

/**
 * View Model for watchlist items
 */
public class WatchlistVM implements Serializable {

    private String symbol;
    private Instant addedAt;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Instant getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Instant addedAt) {
        this.addedAt = addedAt;
    }

    @Override
    public String toString() {
        return "WatchlistVM{" +
                "symbol='" + symbol + '\'' +
                ", addedAt=" + addedAt +
                '}';
    }
}
