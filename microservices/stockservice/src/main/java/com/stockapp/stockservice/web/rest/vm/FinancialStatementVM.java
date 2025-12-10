package com.stockapp.stockservice.web.rest.vm;

import com.stockapp.stockservice.domain.enumeration.FinancialStatementType;
import com.stockapp.stockservice.domain.enumeration.FrequencyType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * View Model for financial statements
 */
@Schema(description = "Financial statement data")
public class FinancialStatementVM implements Serializable {

    @Schema(description = "Stock symbol")
    private String symbol;

    @Schema(description = "Statement type (BS, IC, CF)")
    private FinancialStatementType statementType;

    @Schema(description = "Frequency (ANNUAL, QUARTERLY)")
    private FrequencyType frequency;

    @Schema(description = "Year")
    private Integer year;

    @Schema(description = "Fiscal date ending")
    private LocalDate fiscalDateEnding;

    // Balance Sheet fields
    @Schema(description = "Total assets")
    private BigDecimal totalAssets;

    @Schema(description = "Total liabilities")
    private BigDecimal totalLiabilities;

    @Schema(description = "Total equity")
    private BigDecimal totalEquity;

    // Income Statement fields
    @Schema(description = "Revenue")
    private BigDecimal revenue;

    @Schema(description = "Net income")
    private BigDecimal netIncome;

    @Schema(description = "Operating income")
    private BigDecimal operatingIncome;

    @Schema(description = "Gross profit")
    private BigDecimal grossProfit;

    // Cash Flow fields
    @Schema(description = "Operating cash flow")
    private BigDecimal operatingCashFlow;

    @Schema(description = "Investing cash flow")
    private BigDecimal investingCashFlow;

    @Schema(description = "Financing cash flow")
    private BigDecimal financingCashFlow;

    // Getters and Setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public FinancialStatementType getStatementType() {
        return statementType;
    }

    public void setStatementType(FinancialStatementType statementType) {
        this.statementType = statementType;
    }

    public FrequencyType getFrequency() {
        return frequency;
    }

    public void setFrequency(FrequencyType frequency) {
        this.frequency = frequency;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public LocalDate getFiscalDateEnding() {
        return fiscalDateEnding;
    }

    public void setFiscalDateEnding(LocalDate fiscalDateEnding) {
        this.fiscalDateEnding = fiscalDateEnding;
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

    public BigDecimal getOperatingIncome() {
        return operatingIncome;
    }

    public void setOperatingIncome(BigDecimal operatingIncome) {
        this.operatingIncome = operatingIncome;
    }

    public BigDecimal getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }

    public BigDecimal getOperatingCashFlow() {
        return operatingCashFlow;
    }

    public void setOperatingCashFlow(BigDecimal operatingCashFlow) {
        this.operatingCashFlow = operatingCashFlow;
    }

    public BigDecimal getInvestingCashFlow() {
        return investingCashFlow;
    }

    public void setInvestingCashFlow(BigDecimal investingCashFlow) {
        this.investingCashFlow = investingCashFlow;
    }

    public BigDecimal getFinancingCashFlow() {
        return financingCashFlow;
    }

    public void setFinancingCashFlow(BigDecimal financingCashFlow) {
        this.financingCashFlow = financingCashFlow;
    }
}
