package com.stockapp.stockservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A PeerCompany.
 */
@Document(collection = "peer_company")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PeerCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("peer_symbol")
    private String peerSymbol;

    @Field("company")
    @JsonIgnoreProperties(value = { "historicalPrices", "financials", "recommendations", "peers" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public PeerCompany id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPeerSymbol() {
        return this.peerSymbol;
    }

    public PeerCompany peerSymbol(String peerSymbol) {
        this.setPeerSymbol(peerSymbol);
        return this;
    }

    public void setPeerSymbol(String peerSymbol) {
        this.peerSymbol = peerSymbol;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public PeerCompany company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeerCompany)) {
            return false;
        }
        return getId() != null && getId().equals(((PeerCompany) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeerCompany{" +
            "id=" + getId() +
            ", peerSymbol='" + getPeerSymbol() + "'" +
            "}";
    }
}
