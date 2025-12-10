package com.stockapp.stockservice.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.stockapp.stockservice.domain.PeerCompany} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PeerCompanyDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String peerSymbol;

    private CompanyDTO company;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPeerSymbol() {
        return peerSymbol;
    }

    public void setPeerSymbol(String peerSymbol) {
        this.peerSymbol = peerSymbol;
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
        if (!(o instanceof PeerCompanyDTO)) {
            return false;
        }

        PeerCompanyDTO peerCompanyDTO = (PeerCompanyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, peerCompanyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeerCompanyDTO{" +
            "id='" + getId() + "'" +
            ", peerSymbol='" + getPeerSymbol() + "'" +
            ", company=" + getCompany() +
            "}";
    }
}
