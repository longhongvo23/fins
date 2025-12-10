package com.stockapp.stockservice.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.stockapp.stockservice.domain.StockStatistics} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockStatisticsDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[A-Z0-9\\.]+$")
    private String symbol;

    @Min(value = 0L)
    private Long averageVolume;

    private BigDecimal rolling1dChange;

    private BigDecimal rolling7dChange;

    private BigDecimal rollingChange;

    @DecimalMin(value = "0")
    private BigDecimal fiftyTwoWeekLow;

    @DecimalMin(value = "0")
    private BigDecimal fiftyTwoWeekHigh;

    private BigDecimal fiftyTwoWeekLowChange;

    private BigDecimal fiftyTwoWeekHighChange;

    private BigDecimal fiftyTwoWeekLowChangePercent;

    private BigDecimal fiftyTwoWeekHighChangePercent;

    private String fiftyTwoWeekRange;

    private BigDecimal extendedChange;

    private BigDecimal extendedPercentChange;

    private BigDecimal extendedPrice;

    private Long extendedTimestamp;

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

    public Long getAverageVolume() {
        return averageVolume;
    }

    public void setAverageVolume(Long averageVolume) {
        this.averageVolume = averageVolume;
    }

    public BigDecimal getRolling1dChange() {
        return rolling1dChange;
    }

    public void setRolling1dChange(BigDecimal rolling1dChange) {
        this.rolling1dChange = rolling1dChange;
    }

    public BigDecimal getRolling7dChange() {
        return rolling7dChange;
    }

    public void setRolling7dChange(BigDecimal rolling7dChange) {
        this.rolling7dChange = rolling7dChange;
    }

    public BigDecimal getRollingChange() {
        return rollingChange;
    }

    public void setRollingChange(BigDecimal rollingChange) {
        this.rollingChange = rollingChange;
    }

    public BigDecimal getFiftyTwoWeekLow() {
        return fiftyTwoWeekLow;
    }

    public void setFiftyTwoWeekLow(BigDecimal fiftyTwoWeekLow) {
        this.fiftyTwoWeekLow = fiftyTwoWeekLow;
    }

    public BigDecimal getFiftyTwoWeekHigh() {
        return fiftyTwoWeekHigh;
    }

    public void setFiftyTwoWeekHigh(BigDecimal fiftyTwoWeekHigh) {
        this.fiftyTwoWeekHigh = fiftyTwoWeekHigh;
    }

    public BigDecimal getFiftyTwoWeekLowChange() {
        return fiftyTwoWeekLowChange;
    }

    public void setFiftyTwoWeekLowChange(BigDecimal fiftyTwoWeekLowChange) {
        this.fiftyTwoWeekLowChange = fiftyTwoWeekLowChange;
    }

    public BigDecimal getFiftyTwoWeekHighChange() {
        return fiftyTwoWeekHighChange;
    }

    public void setFiftyTwoWeekHighChange(BigDecimal fiftyTwoWeekHighChange) {
        this.fiftyTwoWeekHighChange = fiftyTwoWeekHighChange;
    }

    public BigDecimal getFiftyTwoWeekLowChangePercent() {
        return fiftyTwoWeekLowChangePercent;
    }

    public void setFiftyTwoWeekLowChangePercent(BigDecimal fiftyTwoWeekLowChangePercent) {
        this.fiftyTwoWeekLowChangePercent = fiftyTwoWeekLowChangePercent;
    }

    public BigDecimal getFiftyTwoWeekHighChangePercent() {
        return fiftyTwoWeekHighChangePercent;
    }

    public void setFiftyTwoWeekHighChangePercent(BigDecimal fiftyTwoWeekHighChangePercent) {
        this.fiftyTwoWeekHighChangePercent = fiftyTwoWeekHighChangePercent;
    }

    public String getFiftyTwoWeekRange() {
        return fiftyTwoWeekRange;
    }

    public void setFiftyTwoWeekRange(String fiftyTwoWeekRange) {
        this.fiftyTwoWeekRange = fiftyTwoWeekRange;
    }

    public BigDecimal getExtendedChange() {
        return extendedChange;
    }

    public void setExtendedChange(BigDecimal extendedChange) {
        this.extendedChange = extendedChange;
    }

    public BigDecimal getExtendedPercentChange() {
        return extendedPercentChange;
    }

    public void setExtendedPercentChange(BigDecimal extendedPercentChange) {
        this.extendedPercentChange = extendedPercentChange;
    }

    public BigDecimal getExtendedPrice() {
        return extendedPrice;
    }

    public void setExtendedPrice(BigDecimal extendedPrice) {
        this.extendedPrice = extendedPrice;
    }

    public Long getExtendedTimestamp() {
        return extendedTimestamp;
    }

    public void setExtendedTimestamp(Long extendedTimestamp) {
        this.extendedTimestamp = extendedTimestamp;
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
        if (!(o instanceof StockStatisticsDTO)) {
            return false;
        }

        StockStatisticsDTO stockStatisticsDTO = (StockStatisticsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stockStatisticsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockStatisticsDTO{" +
            "id='" + getId() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", averageVolume=" + getAverageVolume() +
            ", rolling1dChange=" + getRolling1dChange() +
            ", rolling7dChange=" + getRolling7dChange() +
            ", rollingChange=" + getRollingChange() +
            ", fiftyTwoWeekLow=" + getFiftyTwoWeekLow() +
            ", fiftyTwoWeekHigh=" + getFiftyTwoWeekHigh() +
            ", fiftyTwoWeekLowChange=" + getFiftyTwoWeekLowChange() +
            ", fiftyTwoWeekHighChange=" + getFiftyTwoWeekHighChange() +
            ", fiftyTwoWeekLowChangePercent=" + getFiftyTwoWeekLowChangePercent() +
            ", fiftyTwoWeekHighChangePercent=" + getFiftyTwoWeekHighChangePercent() +
            ", fiftyTwoWeekRange='" + getFiftyTwoWeekRange() + "'" +
            ", extendedChange=" + getExtendedChange() +
            ", extendedPercentChange=" + getExtendedPercentChange() +
            ", extendedPrice=" + getExtendedPrice() +
            ", extendedTimestamp=" + getExtendedTimestamp() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
