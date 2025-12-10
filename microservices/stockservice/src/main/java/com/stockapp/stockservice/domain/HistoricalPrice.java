package com.stockapp.stockservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.TimeSeries;
import org.springframework.data.mongodb.core.timeseries.Granularity;

/**
 * A HistoricalPrice.
 */
@TimeSeries(collection = HistoricalPrice.COLLECTION, timeField = "datetime", metaField = "symbol", granularity = Granularity.HOURS)
@Document(collection = HistoricalPrice.COLLECTION)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoricalPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String COLLECTION = "historical_price_ts";

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[A-Z0-9\\.]+$")
    @Field("symbol")
    @Indexed(name = "symbol_idx")
    private String symbol;

    @NotNull(message = "must not be null")
    @Field("datetime")
    private Instant datetime;

    @NotNull(message = "must not be null")
    @Field("interval")
    private String interval;

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

    @Field("company")
    @JsonIgnoreProperties(value = { "historicalPrices", "financials", "recommendations", "peers" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public HistoricalPrice id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public HistoricalPrice symbol(String symbol) {
        this.setSymbol(symbol);
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Instant getDatetime() {
        return this.datetime;
    }

    public HistoricalPrice datetime(Instant datetime) {
        this.setDatetime(datetime);
        return this;
    }

    public void setDatetime(Instant datetime) {
        this.datetime = datetime;
    }

    public String getInterval() {
        return this.interval;
    }

    public HistoricalPrice interval(String interval) {
        this.setInterval(interval);
        return this;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public BigDecimal getOpen() {
        return this.open;
    }

    public HistoricalPrice open(BigDecimal open) {
        this.setOpen(open);
        return this;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return this.high;
    }

    public HistoricalPrice high(BigDecimal high) {
        this.setHigh(high);
        return this;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return this.low;
    }

    public HistoricalPrice low(BigDecimal low) {
        this.setLow(low);
        return this;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return this.close;
    }

    public HistoricalPrice close(BigDecimal close) {
        this.setClose(close);
        return this;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public Long getVolume() {
        return this.volume;
    }

    public HistoricalPrice volume(Long volume) {
        this.setVolume(volume);
        return this;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public HistoricalPrice company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoricalPrice)) {
            return false;
        }
        return getId() != null && getId().equals(((HistoricalPrice) o).getId());
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoricalPrice{" +
                "id=" + getId() +
                ", symbol='" + getSymbol() + "'" +
                ", datetime='" + getDatetime() + "'" +
                ", interval='" + getInterval() + "'" +
                ", open=" + getOpen() +
                ", high=" + getHigh() +
                ", low=" + getLow() +
                ", close=" + getClose() +
                ", volume=" + getVolume() +
                "}";
    }
}
