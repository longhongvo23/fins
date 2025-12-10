package com.stockapp.crawlservice.service.dto;

import com.stockapp.crawlservice.domain.enumeration.JobStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.stockapp.crawlservice.domain.CrawlJobState} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CrawlJobStateDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String symbol;

    private Instant lastSuccessfulTimestamp;

    private JobStatus lastSyncStatus;

    private String errorLog;

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

    public Instant getLastSuccessfulTimestamp() {
        return lastSuccessfulTimestamp;
    }

    public void setLastSuccessfulTimestamp(Instant lastSuccessfulTimestamp) {
        this.lastSuccessfulTimestamp = lastSuccessfulTimestamp;
    }

    public JobStatus getLastSyncStatus() {
        return lastSyncStatus;
    }

    public void setLastSyncStatus(JobStatus lastSyncStatus) {
        this.lastSyncStatus = lastSyncStatus;
    }

    public String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrawlJobStateDTO)) {
            return false;
        }

        CrawlJobStateDTO crawlJobStateDTO = (CrawlJobStateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, crawlJobStateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrawlJobStateDTO{" +
            "id='" + getId() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", lastSuccessfulTimestamp='" + getLastSuccessfulTimestamp() + "'" +
            ", lastSyncStatus='" + getLastSyncStatus() + "'" +
            ", errorLog='" + getErrorLog() + "'" +
            "}";
    }
}
