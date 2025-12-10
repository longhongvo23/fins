package com.stockapp.stockservice.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A StockStatistics.
 */
@Document(collection = "stock_statistics")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[A-Z0-9\\.]+$")
    @Field("symbol")
    private String symbol;

    @Min(value = 0L)
    @Field("average_volume")
    private Long averageVolume;

    @Field("rolling_1_d_change")
    private BigDecimal rolling1dChange;

    @Field("rolling_7_d_change")
    private BigDecimal rolling7dChange;

    @Field("rolling_change")
    private BigDecimal rollingChange;

    @DecimalMin(value = "0")
    @Field("fifty_two_week_low")
    private BigDecimal fiftyTwoWeekLow;

    @DecimalMin(value = "0")
    @Field("fifty_two_week_high")
    private BigDecimal fiftyTwoWeekHigh;

    @Field("fifty_two_week_low_change")
    private BigDecimal fiftyTwoWeekLowChange;

    @Field("fifty_two_week_high_change")
    private BigDecimal fiftyTwoWeekHighChange;

    @Field("fifty_two_week_low_change_percent")
    private BigDecimal fiftyTwoWeekLowChangePercent;

    @Field("fifty_two_week_high_change_percent")
    private BigDecimal fiftyTwoWeekHighChangePercent;

    @Field("fifty_two_week_range")
    private String fiftyTwoWeekRange;

    @Field("extended_change")
    private BigDecimal extendedChange;

    @Field("extended_percent_change")
    private BigDecimal extendedPercentChange;

    @Field("extended_price")
    private BigDecimal extendedPrice;

    @Field("extended_timestamp")
    private Long extendedTimestamp;

    @NotNull(message = "must not be null")
    @Field("updated_at")
    private Instant updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public StockStatistics id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public StockStatistics symbol(String symbol) {
        this.setSymbol(symbol);
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getAverageVolume() {
        return this.averageVolume;
    }

    public StockStatistics averageVolume(Long averageVolume) {
        this.setAverageVolume(averageVolume);
        return this;
    }

    public void setAverageVolume(Long averageVolume) {
        this.averageVolume = averageVolume;
    }

    public BigDecimal getRolling1dChange() {
        return this.rolling1dChange;
    }

    public StockStatistics rolling1dChange(BigDecimal rolling1dChange) {
        this.setRolling1dChange(rolling1dChange);
        return this;
    }

    public void setRolling1dChange(BigDecimal rolling1dChange) {
        this.rolling1dChange = rolling1dChange;
    }

    public BigDecimal getRolling7dChange() {
        return this.rolling7dChange;
    }

    public StockStatistics rolling7dChange(BigDecimal rolling7dChange) {
        this.setRolling7dChange(rolling7dChange);
        return this;
    }

    public void setRolling7dChange(BigDecimal rolling7dChange) {
        this.rolling7dChange = rolling7dChange;
    }

    public BigDecimal getRollingChange() {
        return this.rollingChange;
    }

    public StockStatistics rollingChange(BigDecimal rollingChange) {
        this.setRollingChange(rollingChange);
        return this;
    }

    public void setRollingChange(BigDecimal rollingChange) {
        this.rollingChange = rollingChange;
    }

    public BigDecimal getFiftyTwoWeekLow() {
        return this.fiftyTwoWeekLow;
    }

    public StockStatistics fiftyTwoWeekLow(BigDecimal fiftyTwoWeekLow) {
        this.setFiftyTwoWeekLow(fiftyTwoWeekLow);
        return this;
    }

    public void setFiftyTwoWeekLow(BigDecimal fiftyTwoWeekLow) {
        this.fiftyTwoWeekLow = fiftyTwoWeekLow;
    }

    public BigDecimal getFiftyTwoWeekHigh() {
        return this.fiftyTwoWeekHigh;
    }

    public StockStatistics fiftyTwoWeekHigh(BigDecimal fiftyTwoWeekHigh) {
        this.setFiftyTwoWeekHigh(fiftyTwoWeekHigh);
        return this;
    }

    public void setFiftyTwoWeekHigh(BigDecimal fiftyTwoWeekHigh) {
        this.fiftyTwoWeekHigh = fiftyTwoWeekHigh;
    }

    public BigDecimal getFiftyTwoWeekLowChange() {
        return this.fiftyTwoWeekLowChange;
    }

    public StockStatistics fiftyTwoWeekLowChange(BigDecimal fiftyTwoWeekLowChange) {
        this.setFiftyTwoWeekLowChange(fiftyTwoWeekLowChange);
        return this;
    }

    public void setFiftyTwoWeekLowChange(BigDecimal fiftyTwoWeekLowChange) {
        this.fiftyTwoWeekLowChange = fiftyTwoWeekLowChange;
    }

    public BigDecimal getFiftyTwoWeekHighChange() {
        return this.fiftyTwoWeekHighChange;
    }

    public StockStatistics fiftyTwoWeekHighChange(BigDecimal fiftyTwoWeekHighChange) {
        this.setFiftyTwoWeekHighChange(fiftyTwoWeekHighChange);
        return this;
    }

    public void setFiftyTwoWeekHighChange(BigDecimal fiftyTwoWeekHighChange) {
        this.fiftyTwoWeekHighChange = fiftyTwoWeekHighChange;
    }

    public BigDecimal getFiftyTwoWeekLowChangePercent() {
        return this.fiftyTwoWeekLowChangePercent;
    }

    public StockStatistics fiftyTwoWeekLowChangePercent(BigDecimal fiftyTwoWeekLowChangePercent) {
        this.setFiftyTwoWeekLowChangePercent(fiftyTwoWeekLowChangePercent);
        return this;
    }

    public void setFiftyTwoWeekLowChangePercent(BigDecimal fiftyTwoWeekLowChangePercent) {
        this.fiftyTwoWeekLowChangePercent = fiftyTwoWeekLowChangePercent;
    }

    public BigDecimal getFiftyTwoWeekHighChangePercent() {
        return this.fiftyTwoWeekHighChangePercent;
    }

    public StockStatistics fiftyTwoWeekHighChangePercent(BigDecimal fiftyTwoWeekHighChangePercent) {
        this.setFiftyTwoWeekHighChangePercent(fiftyTwoWeekHighChangePercent);
        return this;
    }

    public void setFiftyTwoWeekHighChangePercent(BigDecimal fiftyTwoWeekHighChangePercent) {
        this.fiftyTwoWeekHighChangePercent = fiftyTwoWeekHighChangePercent;
    }

    public String getFiftyTwoWeekRange() {
        return this.fiftyTwoWeekRange;
    }

    public StockStatistics fiftyTwoWeekRange(String fiftyTwoWeekRange) {
        this.setFiftyTwoWeekRange(fiftyTwoWeekRange);
        return this;
    }

    public void setFiftyTwoWeekRange(String fiftyTwoWeekRange) {
        this.fiftyTwoWeekRange = fiftyTwoWeekRange;
    }

    public BigDecimal getExtendedChange() {
        return this.extendedChange;
    }

    public StockStatistics extendedChange(BigDecimal extendedChange) {
        this.setExtendedChange(extendedChange);
        return this;
    }

    public void setExtendedChange(BigDecimal extendedChange) {
        this.extendedChange = extendedChange;
    }

    public BigDecimal getExtendedPercentChange() {
        return this.extendedPercentChange;
    }

    public StockStatistics extendedPercentChange(BigDecimal extendedPercentChange) {
        this.setExtendedPercentChange(extendedPercentChange);
        return this;
    }

    public void setExtendedPercentChange(BigDecimal extendedPercentChange) {
        this.extendedPercentChange = extendedPercentChange;
    }

    public BigDecimal getExtendedPrice() {
        return this.extendedPrice;
    }

    public StockStatistics extendedPrice(BigDecimal extendedPrice) {
        this.setExtendedPrice(extendedPrice);
        return this;
    }

    public void setExtendedPrice(BigDecimal extendedPrice) {
        this.extendedPrice = extendedPrice;
    }

    public Long getExtendedTimestamp() {
        return this.extendedTimestamp;
    }

    public StockStatistics extendedTimestamp(Long extendedTimestamp) {
        this.setExtendedTimestamp(extendedTimestamp);
        return this;
    }

    public void setExtendedTimestamp(Long extendedTimestamp) {
        this.extendedTimestamp = extendedTimestamp;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public StockStatistics updatedAt(Instant updatedAt) {
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
        if (!(o instanceof StockStatistics)) {
            return false;
        }
        return getId() != null && getId().equals(((StockStatistics) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockStatistics{" +
            "id=" + getId() +
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
