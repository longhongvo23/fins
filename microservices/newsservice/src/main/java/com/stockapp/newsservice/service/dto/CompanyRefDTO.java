package com.stockapp.newsservice.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.stockapp.newsservice.domain.CompanyRef} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompanyRefDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[A-Z0-9\\.]+$")
    private String symbol;

    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyRefDTO)) {
            return false;
        }

        CompanyRefDTO companyRefDTO = (CompanyRefDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, companyRefDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyRefDTO{" +
            "id='" + getId() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
