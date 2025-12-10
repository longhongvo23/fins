package com.stockapp.stockservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stockapp.stockservice.domain.enumeration.FinancialStatementType;
import com.stockapp.stockservice.domain.enumeration.FrequencyType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Financial.
 */
@Document(collection = "financial")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Financial implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[A-Z0-9\\.]+$")
    @Field("symbol")
    private String symbol;

    @NotNull(message = "must not be null")
    @Min(value = 1900)
    @Field("year")
    private Integer year;

    @NotNull(message = "must not be null")
    @Field("frequency")
    private FrequencyType frequency;

    @NotNull(message = "must not be null")
    @Field("statement_type")
    private FinancialStatementType statementType;

    @Field("total_assets")
    private BigDecimal totalAssets;

    @Field("total_liabilities")
    private BigDecimal totalLiabilities;

    @Field("total_equity")
    private BigDecimal totalEquity;

    @Field("revenue")
    private BigDecimal revenue;

    @Field("net_income")
    private BigDecimal netIncome;

    @Field("operating_cash_flow")
    private BigDecimal operatingCashFlow;

    @Field("filing_date")
    private LocalDate filingDate;

    @Field("fiscal_date_ending")
    private LocalDate fiscalDateEnding;

    @Field("company")
    @JsonIgnoreProperties(value = { "historicalPrices", "financials", "recommendations", "peers" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Financial id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public Financial symbol(String symbol) {
        this.setSymbol(symbol);
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getYear() {
        return this.year;
    }

    public Financial year(Integer year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public FrequencyType getFrequency() {
        return this.frequency;
    }

    public Financial frequency(FrequencyType frequency) {
        this.setFrequency(frequency);
        return this;
    }

    public void setFrequency(FrequencyType frequency) {
        this.frequency = frequency;
    }

    public FinancialStatementType getStatementType() {
        return this.statementType;
    }

    public Financial statementType(FinancialStatementType statementType) {
        this.setStatementType(statementType);
        return this;
    }

    public void setStatementType(FinancialStatementType statementType) {
        this.statementType = statementType;
    }

    public BigDecimal getTotalAssets() {
        return this.totalAssets;
    }

    public Financial totalAssets(BigDecimal totalAssets) {
        this.setTotalAssets(totalAssets);
        return this;
    }

    public void setTotalAssets(BigDecimal totalAssets) {
        this.totalAssets = totalAssets;
    }

    public BigDecimal getTotalLiabilities() {
        return this.totalLiabilities;
    }

    public Financial totalLiabilities(BigDecimal totalLiabilities) {
        this.setTotalLiabilities(totalLiabilities);
        return this;
    }

    public void setTotalLiabilities(BigDecimal totalLiabilities) {
        this.totalLiabilities = totalLiabilities;
    }

    public BigDecimal getTotalEquity() {
        return this.totalEquity;
    }

    public Financial totalEquity(BigDecimal totalEquity) {
        this.setTotalEquity(totalEquity);
        return this;
    }

    public void setTotalEquity(BigDecimal totalEquity) {
        this.totalEquity = totalEquity;
    }

    public BigDecimal getRevenue() {
        return this.revenue;
    }

    public Financial revenue(BigDecimal revenue) {
        this.setRevenue(revenue);
        return this;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public BigDecimal getNetIncome() {
        return this.netIncome;
    }

    public Financial netIncome(BigDecimal netIncome) {
        this.setNetIncome(netIncome);
        return this;
    }

    public void setNetIncome(BigDecimal netIncome) {
        this.netIncome = netIncome;
    }

    public BigDecimal getOperatingCashFlow() {
        return this.operatingCashFlow;
    }

    public Financial operatingCashFlow(BigDecimal operatingCashFlow) {
        this.setOperatingCashFlow(operatingCashFlow);
        return this;
    }

    public void setOperatingCashFlow(BigDecimal operatingCashFlow) {
        this.operatingCashFlow = operatingCashFlow;
    }

    public LocalDate getFilingDate() {
        return this.filingDate;
    }

    public Financial filingDate(LocalDate filingDate) {
        this.setFilingDate(filingDate);
        return this;
    }

    public void setFilingDate(LocalDate filingDate) {
        this.filingDate = filingDate;
    }

    public LocalDate getFiscalDateEnding() {
        return this.fiscalDateEnding;
    }

    public Financial fiscalDateEnding(LocalDate fiscalDateEnding) {
        this.setFiscalDateEnding(fiscalDateEnding);
        return this;
    }

    public void setFiscalDateEnding(LocalDate fiscalDateEnding) {
        this.fiscalDateEnding = fiscalDateEnding;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Financial company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Financial)) {
            return false;
        }
        return getId() != null && getId().equals(((Financial) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Financial{" +
            "id=" + getId() +
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
            "}";
    }
}
