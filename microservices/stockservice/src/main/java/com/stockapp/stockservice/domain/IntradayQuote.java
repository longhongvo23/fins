package com.stockapp.stockservice.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A IntradayQuote.
 */
@Document(collection = "intraday_quote")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IntradayQuote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[A-Z0-9\\.]+$")
    @Field("symbol")
    private String symbol;

    @Field("name")
    private String name;

    @Field("exchange")
    private String exchange;

    @Field("mic_code")
    private String micCode;

    @Field("currency")
    private String currency;

    @NotNull(message = "must not be null")
    @Field("datetime")
    private String datetime;

    @NotNull(message = "must not be null")
    @Field("timestamp")
    private Long timestamp;

    @Field("last_quote_at")
    private Long lastQuoteAt;

    @NotNull(message = "must not be null")
    @DecimalMin(value = "0")
    @Field("open")
    private BigDecimal open;

    @NotNull(message = "must not be null")
    @DecimalMin(value = "0")
    @Field("high")
    private BigDecimal high;

    @NotNull(message = "must not be null")
    @DecimalMin(value = "0")
    @Field("low")
    private BigDecimal low;

    @NotNull(message = "must not be null")
    @DecimalMin(value = "0")
    @Field("close")
    private BigDecimal close;

    @NotNull(message = "must not be null")
    @Min(value = 0L)
    @Field("volume")
    private Long volume;

    @DecimalMin(value = "0")
    @Field("previous_close")
    private BigDecimal previousClose;

    @Field("change")
    private BigDecimal change;

    @Field("percent_change")
    private BigDecimal percentChange;

    @Min(value = 0L)
    @Field("average_volume")
    private Long averageVolume;

    @Field("is_market_open")
    private Boolean isMarketOpen;

    @NotNull(message = "must not be null")
    @Field("updated_at")
    private Instant updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public IntradayQuote id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public IntradayQuote symbol(String symbol) {
        this.setSymbol(symbol);
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return this.name;
    }

    public IntradayQuote name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExchange() {
        return this.exchange;
    }

    public IntradayQuote exchange(String exchange) {
        this.setExchange(exchange);
        return this;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getMicCode() {
        return this.micCode;
    }

    public IntradayQuote micCode(String micCode) {
        this.setMicCode(micCode);
        return this;
    }

    public void setMicCode(String micCode) {
        this.micCode = micCode;
    }

    public String getCurrency() {
        return this.currency;
    }

    public IntradayQuote currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDatetime() {
        return this.datetime;
    }

    public IntradayQuote datetime(String datetime) {
        this.setDatetime(datetime);
        return this;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public IntradayQuote timestamp(Long timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getLastQuoteAt() {
        return this.lastQuoteAt;
    }

    public IntradayQuote lastQuoteAt(Long lastQuoteAt) {
        this.setLastQuoteAt(lastQuoteAt);
        return this;
    }

    public void setLastQuoteAt(Long lastQuoteAt) {
        this.lastQuoteAt = lastQuoteAt;
    }

    public BigDecimal getOpen() {
        return this.open;
    }

    public IntradayQuote open(BigDecimal open) {
        this.setOpen(open);
        return this;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return this.high;
    }

    public IntradayQuote high(BigDecimal high) {
        this.setHigh(high);
        return this;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return this.low;
    }

    public IntradayQuote low(BigDecimal low) {
        this.setLow(low);
        return this;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return this.close;
    }

    public IntradayQuote close(BigDecimal close) {
        this.setClose(close);
        return this;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public Long getVolume() {
        return this.volume;
    }

    public IntradayQuote volume(Long volume) {
        this.setVolume(volume);
        return this;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public BigDecimal getPreviousClose() {
        return this.previousClose;
    }

    public IntradayQuote previousClose(BigDecimal previousClose) {
        this.setPreviousClose(previousClose);
        return this;
    }

    public void setPreviousClose(BigDecimal previousClose) {
        this.previousClose = previousClose;
    }

    public BigDecimal getChange() {
        return this.change;
    }

    public IntradayQuote change(BigDecimal change) {
        this.setChange(change);
        return this;
    }

    public void setChange(BigDecimal change) {
        this.change = change;
    }

    public BigDecimal getPercentChange() {
        return this.percentChange;
    }

    public IntradayQuote percentChange(BigDecimal percentChange) {
        this.setPercentChange(percentChange);
        return this;
    }

    public void setPercentChange(BigDecimal percentChange) {
        this.percentChange = percentChange;
    }

    public Long getAverageVolume() {
        return this.averageVolume;
    }

    public IntradayQuote averageVolume(Long averageVolume) {
        this.setAverageVolume(averageVolume);
        return this;
    }

    public void setAverageVolume(Long averageVolume) {
        this.averageVolume = averageVolume;
    }

    public Boolean getIsMarketOpen() {
        return this.isMarketOpen;
    }

    public IntradayQuote isMarketOpen(Boolean isMarketOpen) {
        this.setIsMarketOpen(isMarketOpen);
        return this;
    }

    public void setIsMarketOpen(Boolean isMarketOpen) {
        this.isMarketOpen = isMarketOpen;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public IntradayQuote updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntradayQuote)) {
            return false;
        }
        return getId() != null && getId().equals(((IntradayQuote) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IntradayQuote{" +
            "id=" + getId() +
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
