package com.stockapp.stockservice.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.stockapp.stockservice.domain.Recommendation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecommendationDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private LocalDate period;

    @Min(value = 0)
    private Integer buy;

    @Min(value = 0)
    private Integer hold;

    @Min(value = 0)
    private Integer sell;

    @Min(value = 0)
    private Integer strongBuy;

    @Min(value = 0)
    private Integer strongSell;

    private CompanyDTO company;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getPeriod() {
        return period;
    }

    public void setPeriod(LocalDate period) {
        this.period = period;
    }

    public Integer getBuy() {
        return buy;
    }

    public void setBuy(Integer buy) {
        this.buy = buy;
    }

    public Integer getHold() {
        return hold;
    }

    public void setHold(Integer hold) {
        this.hold = hold;
    }

    public Integer getSell() {
        return sell;
    }

    public void setSell(Integer sell) {
        this.sell = sell;
    }

    public Integer getStrongBuy() {
        return strongBuy;
    }

    public void setStrongBuy(Integer strongBuy) {
        this.strongBuy = strongBuy;
    }

    public Integer getStrongSell() {
        return strongSell;
    }

    public void setStrongSell(Integer strongSell) {
        this.strongSell = strongSell;
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
        if (!(o instanceof RecommendationDTO)) {
            return false;
        }

        RecommendationDTO recommendationDTO = (RecommendationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recommendationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecommendationDTO{" +
            "id='" + getId() + "'" +
            ", period='" + getPeriod() + "'" +
            ", buy=" + getBuy() +
            ", hold=" + getHold() +
            ", sell=" + getSell() +
            ", strongBuy=" + getStrongBuy() +
            ", strongSell=" + getStrongSell() +
            ", company=" + getCompany() +
            "}";
    }
}
