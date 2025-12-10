package com.stockapp.newsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A CompanyRef.
 */
@Document(collection = "company_ref")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompanyRef implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "^[A-Z0-9\\.]+$")
    @Field("symbol")
    private String symbol;

    @Field("name")
    private String name;

    @Field("companyNews")
    @JsonIgnoreProperties(value = { "entities", "company" }, allowSetters = true)
    private Set<CompanyNews> companyNews = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public CompanyRef id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public CompanyRef symbol(String symbol) {
        this.setSymbol(symbol);
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return this.name;
    }

    public CompanyRef name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CompanyNews> getCompanyNews() {
        return this.companyNews;
    }

    public void setCompanyNews(Set<CompanyNews> companyNews) {
        if (this.companyNews != null) {
            this.companyNews.forEach(i -> i.setCompany(null));
        }
        if (companyNews != null) {
            companyNews.forEach(i -> i.setCompany(this));
        }
        this.companyNews = companyNews;
    }

    public CompanyRef companyNews(Set<CompanyNews> companyNews) {
        this.setCompanyNews(companyNews);
        return this;
    }

    public CompanyRef addCompanyNews(CompanyNews companyNews) {
        this.companyNews.add(companyNews);
        companyNews.setCompany(this);
        return this;
    }

    public CompanyRef removeCompanyNews(CompanyNews companyNews) {
        this.companyNews.remove(companyNews);
        companyNews.setCompany(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyRef)) {
            return false;
        }
        return getId() != null && getId().equals(((CompanyRef) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyRef{" +
            "id=" + getId() +
            ", symbol='" + getSymbol() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
