package com.stockapp.stockservice.web.rest.vm;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * View Model for detailed stock statistics
 */
@Schema(description = "Detailed stock statistics and metrics")
public class StockDetailedStatisticsVM implements Serializable {

    @Schema(description = "Stock symbol")
    private String symbol;

    @Schema(description = "Company name")
    private String name;

    // Valuation metrics
    @Schema(description = "Market capitalization")
    private BigDecimal marketCap;

    @Schema(description = "Price to Earnings ratio")
    private BigDecimal peRatio;

    @Schema(description = "Price to Book ratio")
    private BigDecimal priceToBook;

    @Schema(description = "Dividend yield percentage")
    private BigDecimal dividendYield;

    @Schema(description = "Earnings Per Share")
    private BigDecimal eps;

    @Schema(description = "Beta value")
    private BigDecimal beta;

    // Price metrics
    @Schema(description = "52-week high")
    private BigDecimal fiftyTwoWeekHigh;

    @Schema(description = "52-week low")
    private BigDecimal fiftyTwoWeekLow;

    @Schema(description = "Average volume")
    private Long averageVolume;

    @Schema(description = "Shares outstanding")
    private BigDecimal sharesOutstanding;

    // Financial metrics
    @Schema(description = "Return on Equity")
    private BigDecimal roe;

    @Schema(description = "Return on Assets")
    private BigDecimal roa;

    @Schema(description = "Debt to Equity ratio")
    private BigDecimal debtToEquity;

    @Schema(description = "Current ratio")
    private BigDecimal currentRatio;

    @Schema(description = "Quick ratio")
    private BigDecimal quickRatio;

    // Getters and Setters
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

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(BigDecimal marketCap) {
        this.marketCap = marketCap;
    }

    public BigDecimal getPeRatio() {
        return peRatio;
    }

    public void setPeRatio(BigDecimal peRatio) {
        this.peRatio = peRatio;
    }

    public BigDecimal getPriceToBook() {
        return priceToBook;
    }

    public void setPriceToBook(BigDecimal priceToBook) {
        this.priceToBook = priceToBook;
    }

    public BigDecimal getDividendYield() {
        return dividendYield;
    }

    public void setDividendYield(BigDecimal dividendYield) {
        this.dividendYield = dividendYield;
    }

    public BigDecimal getEps() {
        return eps;
    }

    public void setEps(BigDecimal eps) {
        this.eps = eps;
    }

    public BigDecimal getBeta() {
        return beta;
    }

    public void setBeta(BigDecimal beta) {
        this.beta = beta;
    }

    public BigDecimal getFiftyTwoWeekHigh() {
        return fiftyTwoWeekHigh;
    }

    public void setFiftyTwoWeekHigh(BigDecimal fiftyTwoWeekHigh) {
        this.fiftyTwoWeekHigh = fiftyTwoWeekHigh;
    }

    public BigDecimal getFiftyTwoWeekLow() {
        return fiftyTwoWeekLow;
    }

    public void setFiftyTwoWeekLow(BigDecimal fiftyTwoWeekLow) {
        this.fiftyTwoWeekLow = fiftyTwoWeekLow;
    }

    public Long getAverageVolume() {
        return averageVolume;
    }

    public void setAverageVolume(Long averageVolume) {
        this.averageVolume = averageVolume;
    }

    public BigDecimal getSharesOutstanding() {
        return sharesOutstanding;
    }

    public void setSharesOutstanding(BigDecimal sharesOutstanding) {
        this.sharesOutstanding = sharesOutstanding;
    }

    public BigDecimal getRoe() {
        return roe;
    }

    public void setRoe(BigDecimal roe) {
        this.roe = roe;
    }

    public BigDecimal getRoa() {
        return roa;
    }

    public void setRoa(BigDecimal roa) {
        this.roa = roa;
    }

    public BigDecimal getDebtToEquity() {
        return debtToEquity;
    }

    public void setDebtToEquity(BigDecimal debtToEquity) {
        this.debtToEquity = debtToEquity;
    }

    public BigDecimal getCurrentRatio() {
        return currentRatio;
    }

    public void setCurrentRatio(BigDecimal currentRatio) {
        this.currentRatio = currentRatio;
    }

    public BigDecimal getQuickRatio() {
        return quickRatio;
    }

    public void setQuickRatio(BigDecimal quickRatio) {
        this.quickRatio = quickRatio;
    }
}
