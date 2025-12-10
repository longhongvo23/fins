package com.stockapp.stockservice.web.rest;

import static com.stockapp.stockservice.domain.FinancialAsserts.*;
import static com.stockapp.stockservice.web.rest.TestUtil.createUpdateProxyForBean;
import static com.stockapp.stockservice.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.stockservice.IntegrationTest;
import com.stockapp.stockservice.domain.Financial;
import com.stockapp.stockservice.domain.enumeration.FinancialStatementType;
import com.stockapp.stockservice.domain.enumeration.FrequencyType;
import com.stockapp.stockservice.repository.FinancialRepository;
import com.stockapp.stockservice.service.FinancialService;
import com.stockapp.stockservice.service.dto.FinancialDTO;
import com.stockapp.stockservice.service.mapper.FinancialMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link FinancialResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FinancialResourceIT {

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR = 1900;
    private static final Integer UPDATED_YEAR = 1901;

    private static final FrequencyType DEFAULT_FREQUENCY = FrequencyType.ANNUAL;
    private static final FrequencyType UPDATED_FREQUENCY = FrequencyType.QUARTERLY;

    private static final FinancialStatementType DEFAULT_STATEMENT_TYPE = FinancialStatementType.BS;
    private static final FinancialStatementType UPDATED_STATEMENT_TYPE = FinancialStatementType.IC;

    private static final BigDecimal DEFAULT_TOTAL_ASSETS = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_ASSETS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_LIABILITIES = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_LIABILITIES = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_EQUITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_EQUITY = new BigDecimal(2);

    private static final BigDecimal DEFAULT_REVENUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_REVENUE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_NET_INCOME = new BigDecimal(1);
    private static final BigDecimal UPDATED_NET_INCOME = new BigDecimal(2);

    private static final BigDecimal DEFAULT_OPERATING_CASH_FLOW = new BigDecimal(1);
    private static final BigDecimal UPDATED_OPERATING_CASH_FLOW = new BigDecimal(2);

    private static final LocalDate DEFAULT_FILING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FILING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FISCAL_DATE_ENDING = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FISCAL_DATE_ENDING = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/financials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FinancialRepository financialRepository;

    @Mock
    private FinancialRepository financialRepositoryMock;

    @Autowired
    private FinancialMapper financialMapper;

    @Mock
    private FinancialService financialServiceMock;

    @Autowired
    private WebTestClient webTestClient;

    private Financial financial;

    private Financial insertedFinancial;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Financial createEntity() {
        return new Financial()
            .symbol(DEFAULT_SYMBOL)
            .year(DEFAULT_YEAR)
            .frequency(DEFAULT_FREQUENCY)
            .statementType(DEFAULT_STATEMENT_TYPE)
            .totalAssets(DEFAULT_TOTAL_ASSETS)
            .totalLiabilities(DEFAULT_TOTAL_LIABILITIES)
            .totalEquity(DEFAULT_TOTAL_EQUITY)
            .revenue(DEFAULT_REVENUE)
            .netIncome(DEFAULT_NET_INCOME)
            .operatingCashFlow(DEFAULT_OPERATING_CASH_FLOW)
            .filingDate(DEFAULT_FILING_DATE)
            .fiscalDateEnding(DEFAULT_FISCAL_DATE_ENDING);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Financial createUpdatedEntity() {
        return new Financial()
            .symbol(UPDATED_SYMBOL)
            .year(UPDATED_YEAR)
            .frequency(UPDATED_FREQUENCY)
            .statementType(UPDATED_STATEMENT_TYPE)
            .totalAssets(UPDATED_TOTAL_ASSETS)
            .totalLiabilities(UPDATED_TOTAL_LIABILITIES)
            .totalEquity(UPDATED_TOTAL_EQUITY)
            .revenue(UPDATED_REVENUE)
            .netIncome(UPDATED_NET_INCOME)
            .operatingCashFlow(UPDATED_OPERATING_CASH_FLOW)
            .filingDate(UPDATED_FILING_DATE)
            .fiscalDateEnding(UPDATED_FISCAL_DATE_ENDING);
    }

    @BeforeEach
    void initTest() {
        financial = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFinancial != null) {
            financialRepository.delete(insertedFinancial).block();
            insertedFinancial = null;
        }
    }

    @Test
    void createFinancial() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Financial
        FinancialDTO financialDTO = financialMapper.toDto(financial);
        var returnedFinancialDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(financialDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(FinancialDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Financial in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFinancial = financialMapper.toEntity(returnedFinancialDTO);
        assertFinancialUpdatableFieldsEquals(returnedFinancial, getPersistedFinancial(returnedFinancial));

        insertedFinancial = returnedFinancial;
    }

    @Test
    void createFinancialWithExistingId() throws Exception {
        // Create the Financial with an existing ID
        financial.setId("existing_id");
        FinancialDTO financialDTO = financialMapper.toDto(financial);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(financialDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Financial in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkSymbolIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financial.setSymbol(null);

        // Create the Financial, which fails.
        FinancialDTO financialDTO = financialMapper.toDto(financial);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(financialDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkYearIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financial.setYear(null);

        // Create the Financial, which fails.
        FinancialDTO financialDTO = financialMapper.toDto(financial);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(financialDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkFrequencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financial.setFrequency(null);

        // Create the Financial, which fails.
        FinancialDTO financialDTO = financialMapper.toDto(financial);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(financialDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkStatementTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financial.setStatementType(null);

        // Create the Financial, which fails.
        FinancialDTO financialDTO = financialMapper.toDto(financial);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(financialDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllFinancials() {
        // Initialize the database
        insertedFinancial = financialRepository.save(financial).block();

        // Get all the financialList
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
            .value(hasItem(financial.getId()))
            .jsonPath("$.[*].symbol")
            .value(hasItem(DEFAULT_SYMBOL))
            .jsonPath("$.[*].year")
            .value(hasItem(DEFAULT_YEAR))
            .jsonPath("$.[*].frequency")
            .value(hasItem(DEFAULT_FREQUENCY.toString()))
            .jsonPath("$.[*].statementType")
            .value(hasItem(DEFAULT_STATEMENT_TYPE.toString()))
            .jsonPath("$.[*].totalAssets")
            .value(hasItem(sameNumber(DEFAULT_TOTAL_ASSETS)))
            .jsonPath("$.[*].totalLiabilities")
            .value(hasItem(sameNumber(DEFAULT_TOTAL_LIABILITIES)))
            .jsonPath("$.[*].totalEquity")
            .value(hasItem(sameNumber(DEFAULT_TOTAL_EQUITY)))
            .jsonPath("$.[*].revenue")
            .value(hasItem(sameNumber(DEFAULT_REVENUE)))
            .jsonPath("$.[*].netIncome")
            .value(hasItem(sameNumber(DEFAULT_NET_INCOME)))
            .jsonPath("$.[*].operatingCashFlow")
            .value(hasItem(sameNumber(DEFAULT_OPERATING_CASH_FLOW)))
            .jsonPath("$.[*].filingDate")
            .value(hasItem(DEFAULT_FILING_DATE.toString()))
            .jsonPath("$.[*].fiscalDateEnding")
            .value(hasItem(DEFAULT_FISCAL_DATE_ENDING.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFinancialsWithEagerRelationshipsIsEnabled() {
        when(financialServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(financialServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFinancialsWithEagerRelationshipsIsNotEnabled() {
        when(financialServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(financialRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getFinancial() {
        // Initialize the database
        insertedFinancial = financialRepository.save(financial).block();

        // Get the financial
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, financial.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(financial.getId()))
            .jsonPath("$.symbol")
            .value(is(DEFAULT_SYMBOL))
            .jsonPath("$.year")
            .value(is(DEFAULT_YEAR))
            .jsonPath("$.frequency")
            .value(is(DEFAULT_FREQUENCY.toString()))
            .jsonPath("$.statementType")
            .value(is(DEFAULT_STATEMENT_TYPE.toString()))
            .jsonPath("$.totalAssets")
            .value(is(sameNumber(DEFAULT_TOTAL_ASSETS)))
            .jsonPath("$.totalLiabilities")
            .value(is(sameNumber(DEFAULT_TOTAL_LIABILITIES)))
            .jsonPath("$.totalEquity")
            .value(is(sameNumber(DEFAULT_TOTAL_EQUITY)))
            .jsonPath("$.revenue")
            .value(is(sameNumber(DEFAULT_REVENUE)))
            .jsonPath("$.netIncome")
            .value(is(sameNumber(DEFAULT_NET_INCOME)))
            .jsonPath("$.operatingCashFlow")
            .value(is(sameNumber(DEFAULT_OPERATING_CASH_FLOW)))
            .jsonPath("$.filingDate")
            .value(is(DEFAULT_FILING_DATE.toString()))
            .jsonPath("$.fiscalDateEnding")
            .value(is(DEFAULT_FISCAL_DATE_ENDING.toString()));
    }

    @Test
    void getNonExistingFinancial() {
        // Get the financial
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFinancial() throws Exception {
        // Initialize the database
        insertedFinancial = financialRepository.save(financial).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financial
        Financial updatedFinancial = financialRepository.findById(financial.getId()).block();
        updatedFinancial
            .symbol(UPDATED_SYMBOL)
            .year(UPDATED_YEAR)
            .frequency(UPDATED_FREQUENCY)
            .statementType(UPDATED_STATEMENT_TYPE)
            .totalAssets(UPDATED_TOTAL_ASSETS)
            .totalLiabilities(UPDATED_TOTAL_LIABILITIES)
            .totalEquity(UPDATED_TOTAL_EQUITY)
            .revenue(UPDATED_REVENUE)
            .netIncome(UPDATED_NET_INCOME)
            .operatingCashFlow(UPDATED_OPERATING_CASH_FLOW)
            .filingDate(UPDATED_FILING_DATE)
            .fiscalDateEnding(UPDATED_FISCAL_DATE_ENDING);
        FinancialDTO financialDTO = financialMapper.toDto(updatedFinancial);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, financialDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(financialDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Financial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFinancialToMatchAllProperties(updatedFinancial);
    }

    @Test
    void putNonExistingFinancial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financial.setId(UUID.randomUUID().toString());

        // Create the Financial
        FinancialDTO financialDTO = financialMapper.toDto(financial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, financialDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(financialDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Financial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFinancial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financial.setId(UUID.randomUUID().toString());

        // Create the Financial
        FinancialDTO financialDTO = financialMapper.toDto(financial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(financialDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Financial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFinancial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financial.setId(UUID.randomUUID().toString());

        // Create the Financial
        FinancialDTO financialDTO = financialMapper.toDto(financial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(financialDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Financial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFinancialWithPatch() throws Exception {
        // Initialize the database
        insertedFinancial = financialRepository.save(financial).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financial using partial update
        Financial partialUpdatedFinancial = new Financial();
        partialUpdatedFinancial.setId(financial.getId());

        partialUpdatedFinancial
            .frequency(UPDATED_FREQUENCY)
            .totalAssets(UPDATED_TOTAL_ASSETS)
            .totalLiabilities(UPDATED_TOTAL_LIABILITIES)
            .netIncome(UPDATED_NET_INCOME)
            .operatingCashFlow(UPDATED_OPERATING_CASH_FLOW)
            .fiscalDateEnding(UPDATED_FISCAL_DATE_ENDING);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFinancial.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFinancial))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Financial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFinancialUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFinancial, financial),
            getPersistedFinancial(financial)
        );
    }

    @Test
    void fullUpdateFinancialWithPatch() throws Exception {
        // Initialize the database
        insertedFinancial = financialRepository.save(financial).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financial using partial update
        Financial partialUpdatedFinancial = new Financial();
        partialUpdatedFinancial.setId(financial.getId());

        partialUpdatedFinancial
            .symbol(UPDATED_SYMBOL)
            .year(UPDATED_YEAR)
            .frequency(UPDATED_FREQUENCY)
            .statementType(UPDATED_STATEMENT_TYPE)
            .totalAssets(UPDATED_TOTAL_ASSETS)
            .totalLiabilities(UPDATED_TOTAL_LIABILITIES)
            .totalEquity(UPDATED_TOTAL_EQUITY)
            .revenue(UPDATED_REVENUE)
            .netIncome(UPDATED_NET_INCOME)
            .operatingCashFlow(UPDATED_OPERATING_CASH_FLOW)
            .filingDate(UPDATED_FILING_DATE)
            .fiscalDateEnding(UPDATED_FISCAL_DATE_ENDING);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFinancial.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFinancial))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Financial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFinancialUpdatableFieldsEquals(partialUpdatedFinancial, getPersistedFinancial(partialUpdatedFinancial));
    }

    @Test
    void patchNonExistingFinancial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financial.setId(UUID.randomUUID().toString());

        // Create the Financial
        FinancialDTO financialDTO = financialMapper.toDto(financial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, financialDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(financialDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Financial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFinancial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financial.setId(UUID.randomUUID().toString());

        // Create the Financial
        FinancialDTO financialDTO = financialMapper.toDto(financial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(financialDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Financial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFinancial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financial.setId(UUID.randomUUID().toString());

        // Create the Financial
        FinancialDTO financialDTO = financialMapper.toDto(financial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(financialDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Financial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFinancial() {
        // Initialize the database
        insertedFinancial = financialRepository.save(financial).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the financial
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, financial.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return financialRepository.count().block();
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

    protected Financial getPersistedFinancial(Financial financial) {
        return financialRepository.findById(financial.getId()).block();
    }

    protected void assertPersistedFinancialToMatchAllProperties(Financial expectedFinancial) {
        assertFinancialAllPropertiesEquals(expectedFinancial, getPersistedFinancial(expectedFinancial));
    }

    protected void assertPersistedFinancialToMatchUpdatableProperties(Financial expectedFinancial) {
        assertFinancialAllUpdatablePropertiesEquals(expectedFinancial, getPersistedFinancial(expectedFinancial));
    }
}
