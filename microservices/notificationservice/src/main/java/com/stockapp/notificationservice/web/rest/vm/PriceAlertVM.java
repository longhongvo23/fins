package com.stockapp.notificationservice.web.rest.vm;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * View Model for creating price alert
 */
@Schema(description = "Price alert creation request")
public class PriceAlertVM implements Serializable {

    @NotNull
    @Schema(description = "Stock symbol", required = true, example = "AAPL")
    private String symbol;

    @NotNull
    @Schema(description = "Alert condition: above or below", required = true, example = "above")
    private String condition; // "above" or "below"

    @NotNull
    @Schema(description = "Target price", required = true, example = "150.00")
    private BigDecimal targetPrice;

    @Schema(description = "Notify only once", example = "true")
    private Boolean notifyOnce = true;

    @Schema(description = "Alert message")
    private String message;

    // Getters and Setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public BigDecimal getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(BigDecimal targetPrice) {
        this.targetPrice = targetPrice;
    }

    public Boolean getNotifyOnce() {
        return notifyOnce;
    }

    public void setNotifyOnce(Boolean notifyOnce) {
        this.notifyOnce = notifyOnce;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PriceAlertVM{" +
                "symbol='" + symbol + '\'' +
                ", condition='" + condition + '\'' +
                ", targetPrice=" + targetPrice +
                ", notifyOnce=" + notifyOnce +
                '}';
    }
}
