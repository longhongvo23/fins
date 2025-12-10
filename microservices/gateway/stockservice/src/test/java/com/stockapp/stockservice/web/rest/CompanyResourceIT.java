package com.stockapp.stockservice.web.rest;

import static com.stockapp.stockservice.domain.CompanyAsserts.*;
import static com.stockapp.stockservice.web.rest.TestUtil.createUpdateProxyForBean;
import static com.stockapp.stockservice.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.stockservice.IntegrationTest;
import com.stockapp.stockservice.domain.Company;
import com.stockapp.stockservice.repository.CompanyRepository;
import com.stockapp.stockservice.service.dto.CompanyDTO;
import com.stockapp.stockservice.service.mapper.CompanyMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link CompanyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CompanyResourceIT {

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_EXCHANGE = "AAAAAAAAAA";
    private static final String UPDATED_EXCHANGE = "BBBBBBBBBB";

    private static final String DEFAULT_FINNHUB_INDUSTRY = "AAAAAAAAAA";
    private static final String UPDATED_FINNHUB_INDUSTRY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_IPO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_IPO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MARKET_CAPITALIZATION = new BigDecimal(0);
    private static final BigDecimal UPDATED_MARKET_CAPITALIZATION = new BigDecimal(1);

    private static final BigDecimal DEFAULT_SHARE_OUTSTANDING = new BigDecimal(0);
    private static final BigDecimal UPDATED_SHARE_OUTSTANDING = new BigDecimal(1);

    private static final String DEFAULT_WEBURL = "AAAAAAAAAA";
    private static final String UPDATED_WEBURL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/companies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Company company;

    private Company insertedCompany;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createEntity() {
        return new Company()
            .symbol(DEFAULT_SYMBOL)
            .name(DEFAULT_NAME)
            .country(DEFAULT_COUNTRY)
            .currency(DEFAULT_CURRENCY)
            .exchange(DEFAULT_EXCHANGE)
            .finnhubIndustry(DEFAULT_FINNHUB_INDUSTRY)
            .ipo(DEFAULT_IPO)
            .logo(DEFAULT_LOGO)
            .marketCapitalization(DEFAULT_MARKET_CAPITALIZATION)
            .shareOutstanding(DEFAULT_SHARE_OUTSTANDING)
            .weburl(DEFAULT_WEBURL)
            .phone(DEFAULT_PHONE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createUpdatedEntity() {
        return new Company()
            .symbol(UPDATED_SYMBOL)
            .name(UPDATED_NAME)
            .country(UPDATED_COUNTRY)
            .currency(UPDATED_CURRENCY)
            .exchange(UPDATED_EXCHANGE)
            .finnhubIndustry(UPDATED_FINNHUB_INDUSTRY)
            .ipo(UPDATED_IPO)
            .logo(UPDATED_LOGO)
            .marketCapitalization(UPDATED_MARKET_CAPITALIZATION)
            .shareOutstanding(UPDATED_SHARE_OUTSTANDING)
            .weburl(UPDATED_WEBURL)
            .phone(UPDATED_PHONE);
    }

    @BeforeEach
    void initTest() {
        company = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCompany != null) {
            companyRepository.delete(insertedCompany).block();
            insertedCompany = null;
        }
    }

    @Test
    void createCompany() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);
        var returnedCompanyDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(CompanyDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Company in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCompany = companyMapper.toEntity(returnedCompanyDTO);
        assertCompanyUpdatableFieldsEquals(returnedCompany, getPersistedCompany(returnedCompany));

        insertedCompany = returnedCompany;
    }

    @Test
    void createCompanyWithExistingId() throws Exception {
        // Create the Company with an existing ID
        company.setId("existing_id");
        CompanyDTO companyDTO = companyMapper.toDto(company);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkSymbolIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        company.setSymbol(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        company.setName(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.toDto(company);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllCompanies() {
        // Initialize the database
        insertedCompany = companyRepository.save(company).block();

        // Get all the companyList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(company.getId()))
            .jsonPath("$.[*].symbol")
            .value(hasItem(DEFAULT_SYMBOL))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].country")
            .value(hasItem(DEFAULT_COUNTRY))
            .jsonPath("$.[*].currency")
            .value(hasItem(DEFAULT_CURRENCY))
            .jsonPath("$.[*].exchange")
            .value(hasItem(DEFAULT_EXCHANGE))
            .jsonPath("$.[*].finnhubIndustry")
            .value(hasItem(DEFAULT_FINNHUB_INDUSTRY))
            .jsonPath("$.[*].ipo")
            .value(hasItem(DEFAULT_IPO.toString()))
            .jsonPath("$.[*].logo")
            .value(hasItem(DEFAULT_LOGO))
            .jsonPath("$.[*].marketCapitalization")
            .value(hasItem(sameNumber(DEFAULT_MARKET_CAPITALIZATION)))
            .jsonPath("$.[*].shareOutstanding")
            .value(hasItem(sameNumber(DEFAULT_SHARE_OUTSTANDING)))
            .jsonPath("$.[*].weburl")
            .value(hasItem(DEFAULT_WEBURL))
            .jsonPath("$.[*].phone")
            .value(hasItem(DEFAULT_PHONE));
    }

    @Test
    void getCompany() {
        // Initialize the database
        insertedCompany = companyRepository.save(company).block();

        // Get the company
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, company.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(company.getId()))
            .jsonPath("$.symbol")
            .value(is(DEFAULT_SYMBOL))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.country")
            .value(is(DEFAULT_COUNTRY))
            .jsonPath("$.currency")
            .value(is(DEFAULT_CURRENCY))
            .jsonPath("$.exchange")
            .value(is(DEFAULT_EXCHANGE))
            .jsonPath("$.finnhubIndustry")
            .value(is(DEFAULT_FINNHUB_INDUSTRY))
            .jsonPath("$.ipo")
            .value(is(DEFAULT_IPO.toString()))
            .jsonPath("$.logo")
            .value(is(DEFAULT_LOGO))
            .jsonPath("$.marketCapitalization")
            .value(is(sameNumber(DEFAULT_MARKET_CAPITALIZATION)))
            .jsonPath("$.shareOutstanding")
            .value(is(sameNumber(DEFAULT_SHARE_OUTSTANDING)))
            .jsonPath("$.weburl")
            .value(is(DEFAULT_WEBURL))
            .jsonPath("$.phone")
            .value(is(DEFAULT_PHONE));
    }

    @Test
    void getNonExistingCompany() {
        // Get the company
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCompany() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.save(company).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the company
        Company updatedCompany = companyRepository.findById(company.getId()).block();
        updatedCompany
            .symbol(UPDATED_SYMBOL)
            .name(UPDATED_NAME)
            .country(UPDATED_COUNTRY)
            .currency(UPDATED_CURRENCY)
            .exchange(UPDATED_EXCHANGE)
            .finnhubIndustry(UPDATED_FINNHUB_INDUSTRY)
            .ipo(UPDATED_IPO)
            .logo(UPDATED_LOGO)
            .marketCapitalization(UPDATED_MARKET_CAPITALIZATION)
            .shareOutstanding(UPDATED_SHARE_OUTSTANDING)
            .weburl(UPDATED_WEBURL)
            .phone(UPDATED_PHONE);
        CompanyDTO companyDTO = companyMapper.toDto(updatedCompany);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, companyDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompanyToMatchAllProperties(updatedCompany);
    }

    @Test
    void putNonExistingCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(UUID.randomUUID().toString());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, companyDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(UUID.randomUUID().toString());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(UUID.randomUUID().toString());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.save(company).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany
            .symbol(UPDATED_SYMBOL)
            .country(UPDATED_COUNTRY)
            .ipo(UPDATED_IPO)
            .marketCapitalization(UPDATED_MARKET_CAPITALIZATION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCompany))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Company in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompanyUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCompany, company), getPersistedCompany(company));
    }

    @Test
    void fullUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.save(company).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany
            .symbol(UPDATED_SYMBOL)
            .name(UPDATED_NAME)
            .country(UPDATED_COUNTRY)
            .currency(UPDATED_CURRENCY)
            .exchange(UPDATED_EXCHANGE)
            .finnhubIndustry(UPDATED_FINNHUB_INDUSTRY)
            .ipo(UPDATED_IPO)
            .logo(UPDATED_LOGO)
            .marketCapitalization(UPDATED_MARKET_CAPITALIZATION)
            .shareOutstanding(UPDATED_SHARE_OUTSTANDING)
            .weburl(UPDATED_WEBURL)
            .phone(UPDATED_PHONE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCompany))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Company in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompanyUpdatableFieldsEquals(partialUpdatedCompany, getPersistedCompany(partialUpdatedCompany));
    }

    @Test
    void patchNonExistingCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(UUID.randomUUID().toString());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, companyDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(UUID.randomUUID().toString());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(UUID.randomUUID().toString());

        // Create the Company
        CompanyDTO companyDTO = companyMapper.toDto(company);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(companyDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCompany() {
        // Initialize the database
        insertedCompany = companyRepository.save(company).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the company
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, company.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return companyRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Company getPersistedCompany(Company company) {
        return companyRepository.findById(company.getId()).block();
    }

    protected void assertPersistedCompanyToMatchAllProperties(Company expectedCompany) {
        assertCompanyAllPropertiesEquals(expectedCompany, getPersistedCompany(expectedCompany));
    }

    protected void assertPersistedCompanyToMatchUpdatableProperties(Company expectedCompany) {
        assertCompanyAllUpdatablePropertiesEquals(expectedCompany, getPersistedCompany(expectedCompany));
    }
}
