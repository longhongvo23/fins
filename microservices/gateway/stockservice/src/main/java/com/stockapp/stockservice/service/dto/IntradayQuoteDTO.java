package com.stockapp.stockservice.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.stockapp.stockservice.domain.IntradayQuote} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IntradayQuoteDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[A-Z0-9\\.]+$")
    private String symbol;

    private String name;

    private String exchange;

    private String micCode;

    private String currency;

    @NotNull(message = "must not be null")
    private String datetime;

    @NotNull(message = "must not be null")
    private Long timestamp;

    private Long lastQuoteAt;

    @NotNull(message = "must not be null")
    @DecimalMin(value = "0")
    private BigDecimal open;

    @NotNull(message = "must not be null")
    @DecimalMin(value = "0")
    private BigDecimal high;

    @NotNull(message = "must not be null")
    @DecimalMin(value = "0")
    private BigDecimal low;

    @NotNull(message = "must not be null")
    @DecimalMin(value = "0")
    private BigDecimal close;

    @NotNull(message = "must not be null")
    @Min(value = 0L)
    private Long volume;

    @DecimalMin(value = "0")
    private BigDecimal previousClose;

    private BigDecimal change;

    private BigDecimal percentChange;

    @Min(value = 0L)
    private Long averageVolume;

    private Boolean isMarketOpen;

    @NotNull(message = "must not be null")
    private Instant updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Long getLastQuoteAt() {
        return lastQuoteAt;
    }

    public void setLastQuoteAt(Long lastQuoteAt) {
        this.lastQuoteAt = lastQuoteAt;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
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

    public BigDecimal getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(BigDecimal percentChange) {
        this.percentChange = percentChange;
    }

    public Long getAverageVolume() {
        return averageVolume;
    }

    public void setAverageVolume(Long averageVolume) {
        this.averageVolume = averageVolume;
    }

    public Boolean getIsMarketOpen() {
        return isMarketOpen;
    }

    public void setIsMarketOpen(Boolean isMarketOpen) {
        this.isMarketOpen = isMarketOpen;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntradayQuoteDTO)) {
            return false;
        }

        IntradayQuoteDTO intradayQuoteDTO = (IntradayQuoteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, intradayQuoteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IntradayQuoteDTO{" +
            "id='" + getId() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", name='" + getName() + "'" +
            ", exchange='" + getExchange() + "'" +
            ", micCode='" + getMicCode() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", datetime='" + getDatetime() + "'" +
            ", timestamp=" + getTimestamp() +
            ", lastQuoteAt=" + getLastQuoteAt() +
            ", open=" + getOpen() +
            ", high=" + getHigh() +
            ", low=" + getLow() +
            ", close=" + getClose() +
            ", volume=" + getVolume() +
            ", previousClose=" + getPreviousClose() +
            ", change=" + getChange() +
            ", percentChange=" + getPercentChange() +
            ", averageVolume=" + getAverageVolume() +
            ", isMarketOpen='" + getIsMarketOpen() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
