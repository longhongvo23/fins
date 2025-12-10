package com.stockapp.stockservice.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.stockapp.stockservice.domain.HistoricalPrice} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoricalPriceDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[A-Z0-9\\.]+$")
    private String symbol;

    @NotNull(message = "must not be null")
    private String datetime;

    @NotNull(message = "must not be null")
    private String interval;

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

    private CompanyDTO company;

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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
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

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoricalPriceDTO)) {
            return false;
        }

        HistoricalPriceDTO historicalPriceDTO = (HistoricalPriceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historicalPriceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoricalPriceDTO{" +
            "id='" + getId() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", datetime='" + getDatetime() + "'" +
            ", interval='" + getInterval() + "'" +
            ", open=" + getOpen() +
            ", high=" + getHigh() +
            ", low=" + getLow() +
            ", close=" + getClose() +
            ", volume=" + getVolume() +
            ", company=" + getCompany() +
            "}";
    }
}
