package com.stockapp.stockservice.web.rest.vm;

import java.io.Serializable;

/**
 * View Model for stock search results
 */
public class StockSearchResultVM implements Serializable {

    private String symbol;
    private String name;
    private String exchange;
    private String logo;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "StockSearchResultVM{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", exchange='" + exchange + '\'' +
                '}';
    }
}
