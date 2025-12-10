package com.stockapp.stockservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Recommendation.
 */
@Document(collection = "recommendation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Recommendation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("period")
    private LocalDate period;

    @Min(value = 0)
    @Field("buy")
    private Integer buy;

    @Min(value = 0)
    @Field("hold")
    private Integer hold;

    @Min(value = 0)
    @Field("sell")
    private Integer sell;

    @Min(value = 0)
    @Field("strong_buy")
    private Integer strongBuy;

    @Min(value = 0)
    @Field("strong_sell")
    private Integer strongSell;

    @Field("company")
    @JsonIgnoreProperties(value = { "historicalPrices", "financials", "recommendations", "peers" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Recommendation id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getPeriod() {
        return this.period;
    }

    public Recommendation period(LocalDate period) {
        this.setPeriod(period);
        return this;
    }

    public void setPeriod(LocalDate period) {
        this.period = period;
    }

    public Integer getBuy() {
        return this.buy;
    }

    public Recommendation buy(Integer buy) {
        this.setBuy(buy);
        return this;
    }

    public void setBuy(Integer buy) {
        this.buy = buy;
    }

    public Integer getHold() {
        return this.hold;
    }

    public Recommendation hold(Integer hold) {
        this.setHold(hold);
        return this;
    }

    public void setHold(Integer hold) {
        this.hold = hold;
    }

    public Integer getSell() {
        return this.sell;
    }

    public Recommendation sell(Integer sell) {
        this.setSell(sell);
        return this;
    }

    public void setSell(Integer sell) {
        this.sell = sell;
    }

    public Integer getStrongBuy() {
        return this.strongBuy;
    }

    public Recommendation strongBuy(Integer strongBuy) {
        this.setStrongBuy(strongBuy);
        return this;
    }

    public void setStrongBuy(Integer strongBuy) {
        this.strongBuy = strongBuy;
    }

    public Integer getStrongSell() {
        return this.strongSell;
    }

    public Recommendation strongSell(Integer strongSell) {
        this.setStrongSell(strongSell);
        return this;
    }

    public void setStrongSell(Integer strongSell) {
        this.strongSell = strongSell;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Recommendation company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recommendation)) {
            return false;
        }
        return getId() != null && getId().equals(((Recommendation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Recommendation{" +
            "id=" + getId() +
            ", period='" + getPeriod() + "'" +
            ", buy=" + getBuy() +
            ", hold=" + getHold() +
            ", sell=" + getSell() +
            ", strongBuy=" + getStrongBuy() +
            ", strongSell=" + getStrongSell() +
            "}";
    }
}
