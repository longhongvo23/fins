package com.stockapp.stockservice.service.dto;

import com.stockapp.stockservice.domain.enumeration.FinancialStatementType;
import com.stockapp.stockservice.domain.enumeration.FrequencyType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.stockapp.stockservice.domain.Financial} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FinancialDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[A-Z0-9\\.]+$")
    private String symbol;

    @NotNull(message = "must not be null")
    @Min(value = 1900)
    private Integer year;

    @NotNull(message = "must not be null")
    private FrequencyType frequency;

    @NotNull(message = "must not be null")
    private FinancialStatementType statementType;

    private BigDecimal totalAssets;

    private BigDecimal totalLiabilities;

    private BigDecimal totalEquity;

    private BigDecimal revenue;

    private BigDecimal netIncome;

    private BigDecimal operatingCashFlow;

    private LocalDate filingDate;

    private LocalDate fiscalDateEnding;

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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public FrequencyType getFrequency() {
        return frequency;
    }

    public void setFrequency(FrequencyType frequency) {
        this.frequency = frequency;
    }

    public FinancialStatementType getStatementType() {
        return statementType;
    }

    public void setStatementType(FinancialStatementType statementType) {
        this.statementType = statementType;
    }

    public BigDecimal getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(BigDecimal totalAssets) {
        this.totalAssets = totalAssets;
    }

    public BigDecimal getTotalLiabilities() {
        return totalLiabilities;
    }

    public void setTotalLiabilities(BigDecimal totalLiabilities) {
        this.totalLiabilities = totalLiabilities;
    }

    public BigDecimal getTotalEquity() {
        return totalEquity;
    }

    public void setTotalEquity(BigDecimal totalEquity) {
        this.totalEquity = totalEquity;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public BigDecimal getNetIncome() {
        return netIncome;
    }

    public void setNetIncome(BigDecimal netIncome) {
        this.netIncome = netIncome;
    }

    public BigDecimal getOperatingCashFlow() {
        return operatingCashFlow;
    }

    public void setOperatingCashFlow(BigDecimal operatingCashFlow) {
        this.operatingCashFlow = operatingCashFlow;
    }

    public LocalDate getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(LocalDate filingDate) {
        this.filingDate = filingDate;
    }

    public LocalDate getFiscalDateEnding() {
        return fiscalDateEnding;
    }

    public void setFiscalDateEnding(LocalDate fiscalDateEnding) {
        this.fiscalDateEnding = fiscalDateEnding;
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
        if (!(o instanceof FinancialDTO)) {
            return false;
        }

        FinancialDTO financialDTO = (FinancialDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, financialDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinancialDTO{" +
            "id='" + getId() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", year=" + getYear() +
            ", frequency='" + getFrequency() + "'" +
            ", statementType='" + getStatementType() + "'" +
            ", totalAssets=" + getTotalAssets() +
            ", totalLiabilities=" + getTotalLiabilities() +
            ", totalEquity=" + getTotalEquity() +
            ", revenue=" + getRevenue() +
            ", netIncome=" + getNetIncome() +
            ", operatingCashFlow=" + getOperatingCashFlow() +
            ", filingDate='" + getFilingDate() + "'" +
            ", fiscalDateEnding='" + getFiscalDateEnding() + "'" +
            ", company=" + getCompany() +
            "}";
    }
}
