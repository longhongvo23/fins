package com.stockapp.stockservice.web.rest;

import static com.stockapp.stockservice.domain.StockStatisticsAsserts.*;
import static com.stockapp.stockservice.web.rest.TestUtil.createUpdateProxyForBean;
import static com.stockapp.stockservice.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.stockservice.IntegrationTest;
import com.stockapp.stockservice.domain.StockStatistics;
import com.stockapp.stockservice.repository.StockStatisticsRepository;
import com.stockapp.stockservice.service.dto.StockStatisticsDTO;
import com.stockapp.stockservice.service.mapper.StockStatisticsMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link StockStatisticsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class StockStatisticsResourceIT {

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final Long DEFAULT_AVERAGE_VOLUME = 0L;
    private static final Long UPDATED_AVERAGE_VOLUME = 1L;

    private static final BigDecimal DEFAULT_ROLLING_1_D_CHANGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ROLLING_1_D_CHANGE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ROLLING_7_D_CHANGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ROLLING_7_D_CHANGE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ROLLING_CHANGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ROLLING_CHANGE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_FIFTY_TWO_WEEK_LOW = new BigDecimal(0);
    private static final BigDecimal UPDATED_FIFTY_TWO_WEEK_LOW = new BigDecimal(1);

    private static final BigDecimal DEFAULT_FIFTY_TWO_WEEK_HIGH = new BigDecimal(0);
    private static final BigDecimal UPDATED_FIFTY_TWO_WEEK_HIGH = new BigDecimal(1);

    private static final BigDecimal DEFAULT_FIFTY_TWO_WEEK_LOW_CHANGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_FIFTY_TWO_WEEK_LOW_CHANGE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_FIFTY_TWO_WEEK_HIGH_CHANGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_FIFTY_TWO_WEEK_HIGH_CHANGE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_FIFTY_TWO_WEEK_LOW_CHANGE_PERCENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_FIFTY_TWO_WEEK_LOW_CHANGE_PERCENT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_FIFTY_TWO_WEEK_HIGH_CHANGE_PERCENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_FIFTY_TWO_WEEK_HIGH_CHANGE_PERCENT = new BigDecimal(2);

    private static final String DEFAULT_FIFTY_TWO_WEEK_RANGE = "AAAAAAAAAA";
    private static final String UPDATED_FIFTY_TWO_WEEK_RANGE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_EXTENDED_CHANGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_EXTENDED_CHANGE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_EXTENDED_PERCENT_CHANGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_EXTENDED_PERCENT_CHANGE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_EXTENDED_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_EXTENDED_PRICE = new BigDecimal(2);

    private static final Long DEFAULT_EXTENDED_TIMESTAMP = 1L;
    private static final Long UPDATED_EXTENDED_TIMESTAMP = 2L;

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/stock-statistics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StockStatisticsRepository stockStatisticsRepository;

    @Autowired
    private StockStatisticsMapper stockStatisticsMapper;

    @Autowired
    private WebTestClient webTestClient;

    private StockStatistics stockStatistics;

    private StockStatistics insertedStockStatistics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockStatistics createEntity() {
        return new StockStatistics()
            .symbol(DEFAULT_SYMBOL)
            .averageVolume(DEFAULT_AVERAGE_VOLUME)
            .rolling1dChange(DEFAULT_ROLLING_1_D_CHANGE)
            .rolling7dChange(DEFAULT_ROLLING_7_D_CHANGE)
            .rollingChange(DEFAULT_ROLLING_CHANGE)
            .fiftyTwoWeekLow(DEFAULT_FIFTY_TWO_WEEK_LOW)
            .fiftyTwoWeekHigh(DEFAULT_FIFTY_TWO_WEEK_HIGH)
            .fiftyTwoWeekLowChange(DEFAULT_FIFTY_TWO_WEEK_LOW_CHANGE)
            .fiftyTwoWeekHighChange(DEFAULT_FIFTY_TWO_WEEK_HIGH_CHANGE)
            .fiftyTwoWeekLowChangePercent(DEFAULT_FIFTY_TWO_WEEK_LOW_CHANGE_PERCENT)
            .fiftyTwoWeekHighChangePercent(DEFAULT_FIFTY_TWO_WEEK_HIGH_CHANGE_PERCENT)
            .fiftyTwoWeekRange(DEFAULT_FIFTY_TWO_WEEK_RANGE)
            .extendedChange(DEFAULT_EXTENDED_CHANGE)
            .extendedPercentChange(DEFAULT_EXTENDED_PERCENT_CHANGE)
            .extendedPrice(DEFAULT_EXTENDED_PRICE)
            .extendedTimestamp(DEFAULT_EXTENDED_TIMESTAMP)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockStatistics createUpdatedEntity() {
        return new StockStatistics()
            .symbol(UPDATED_SYMBOL)
            .averageVolume(UPDATED_AVERAGE_VOLUME)
            .rolling1dChange(UPDATED_ROLLING_1_D_CHANGE)
            .rolling7dChange(UPDATED_ROLLING_7_D_CHANGE)
            .rollingChange(UPDATED_ROLLING_CHANGE)
            .fiftyTwoWeekLow(UPDATED_FIFTY_TWO_WEEK_LOW)
            .fiftyTwoWeekHigh(UPDATED_FIFTY_TWO_WEEK_HIGH)
            .fiftyTwoWeekLowChange(UPDATED_FIFTY_TWO_WEEK_LOW_CHANGE)
            .fiftyTwoWeekHighChange(UPDATED_FIFTY_TWO_WEEK_HIGH_CHANGE)
            .fiftyTwoWeekLowChangePercent(UPDATED_FIFTY_TWO_WEEK_LOW_CHANGE_PERCENT)
            .fiftyTwoWeekHighChangePercent(UPDATED_FIFTY_TWO_WEEK_HIGH_CHANGE_PERCENT)
            .fiftyTwoWeekRange(UPDATED_FIFTY_TWO_WEEK_RANGE)
            .extendedChange(UPDATED_EXTENDED_CHANGE)
            .extendedPercentChange(UPDATED_EXTENDED_PERCENT_CHANGE)
            .extendedPrice(UPDATED_EXTENDED_PRICE)
            .extendedTimestamp(UPDATED_EXTENDED_TIMESTAMP)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    void initTest() {
        stockStatistics = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedStockStatistics != null) {
            stockStatisticsRepository.delete(insertedStockStatistics).block();
            insertedStockStatistics = null;
        }
    }

    @Test
    void createStockStatistics() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StockStatistics
        StockStatisticsDTO stockStatisticsDTO = stockStatisticsMapper.toDto(stockStatistics);
        var returnedStockStatisticsDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(stockStatisticsDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(StockStatisticsDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the StockStatistics in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStockStatistics = stockStatisticsMapper.toEntity(returnedStockStatisticsDTO);
        assertStockStatisticsUpdatableFieldsEquals(returnedStockStatistics, getPersistedStockStatistics(returnedStockStatistics));

        insertedStockStatistics = returnedStockStatistics;
    }

    @Test
    void createStockStatisticsWithExistingId() throws Exception {
        // Create the StockStatistics with an existing ID
        stockStatistics.setId("existing_id");
        StockStatisticsDTO stockStatisticsDTO = stockStatisticsMapper.toDto(stockStatistics);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(stockStatisticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the StockStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkSymbolIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stockStatistics.setSymbol(null);

        // Create the StockStatistics, which fails.
        StockStatisticsDTO stockStatisticsDTO = stockStatisticsMapper.toDto(stockStatistics);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(stockStatisticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stockStatistics.setUpdatedAt(null);

        // Create the StockStatistics, which fails.
        StockStatisticsDTO stockStatisticsDTO = stockStatisticsMapper.toDto(stockStatistics);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(stockStatisticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllStockStatistics() {
        // Initialize the database
        insertedStockStatistics = stockStatisticsRepository.save(stockStatistics).block();

        // Get all the stockStatisticsList
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
            .value(hasItem(stockStatistics.getId()))
            .jsonPath("$.[*].symbol")
            .value(hasItem(DEFAULT_SYMBOL))
            .jsonPath("$.[*].averageVolume")
            .value(hasItem(DEFAULT_AVERAGE_VOLUME.intValue()))
            .jsonPath("$.[*].rolling1dChange")
            .value(hasItem(sameNumber(DEFAULT_ROLLING_1_D_CHANGE)))
            .jsonPath("$.[*].rolling7dChange")
            .value(hasItem(sameNumber(DEFAULT_ROLLING_7_D_CHANGE)))
            .jsonPath("$.[*].rollingChange")
            .value(hasItem(sameNumber(DEFAULT_ROLLING_CHANGE)))
            .jsonPath("$.[*].fiftyTwoWeekLow")
            .value(hasItem(sameNumber(DEFAULT_FIFTY_TWO_WEEK_LOW)))
            .jsonPath("$.[*].fiftyTwoWeekHigh")
            .value(hasItem(sameNumber(DEFAULT_FIFTY_TWO_WEEK_HIGH)))
            .jsonPath("$.[*].fiftyTwoWeekLowChange")
            .value(hasItem(sameNumber(DEFAULT_FIFTY_TWO_WEEK_LOW_CHANGE)))
            .jsonPath("$.[*].fiftyTwoWeekHighChange")
            .value(hasItem(sameNumber(DEFAULT_FIFTY_TWO_WEEK_HIGH_CHANGE)))
            .jsonPath("$.[*].fiftyTwoWeekLowChangePercent")
            .value(hasItem(sameNumber(DEFAULT_FIFTY_TWO_WEEK_LOW_CHANGE_PERCENT)))
            .jsonPath("$.[*].fiftyTwoWeekHighChangePercent")
            .value(hasItem(sameNumber(DEFAULT_FIFTY_TWO_WEEK_HIGH_CHANGE_PERCENT)))
            .jsonPath("$.[*].fiftyTwoWeekRange")
            .value(hasItem(DEFAULT_FIFTY_TWO_WEEK_RANGE))
            .jsonPath("$.[*].extendedChange")
            .value(hasItem(sameNumber(DEFAULT_EXTENDED_CHANGE)))
            .jsonPath("$.[*].extendedPercentChange")
            .value(hasItem(sameNumber(DEFAULT_EXTENDED_PERCENT_CHANGE)))
            .jsonPath("$.[*].extendedPrice")
            .value(hasItem(sameNumber(DEFAULT_EXTENDED_PRICE)))
            .jsonPath("$.[*].extendedTimestamp")
            .value(hasItem(DEFAULT_EXTENDED_TIMESTAMP.intValue()))
            .jsonPath("$.[*].updatedAt")
            .value(hasItem(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    void getStockStatistics() {
        // Initialize the database
        insertedStockStatistics = stockStatisticsRepository.save(stockStatistics).block();

        // Get the stockStatistics
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, stockStatistics.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(stockStatistics.getId()))
            .jsonPath("$.symbol")
            .value(is(DEFAULT_SYMBOL))
            .jsonPath("$.averageVolume")
            .value(is(DEFAULT_AVERAGE_VOLUME.intValue()))
            .jsonPath("$.rolling1dChange")
            .value(is(sameNumber(DEFAULT_ROLLING_1_D_CHANGE)))
            .jsonPath("$.rolling7dChange")
            .value(is(sameNumber(DEFAULT_ROLLING_7_D_CHANGE)))
            .jsonPath("$.rollingChange")
            .value(is(sameNumber(DEFAULT_ROLLING_CHANGE)))
            .jsonPath("$.fiftyTwoWeekLow")
            .value(is(sameNumber(DEFAULT_FIFTY_TWO_WEEK_LOW)))
            .jsonPath("$.fiftyTwoWeekHigh")
            .value(is(sameNumber(DEFAULT_FIFTY_TWO_WEEK_HIGH)))
            .jsonPath("$.fiftyTwoWeekLowChange")
            .value(is(sameNumber(DEFAULT_FIFTY_TWO_WEEK_LOW_CHANGE)))
            .jsonPath("$.fiftyTwoWeekHighChange")
            .value(is(sameNumber(DEFAULT_FIFTY_TWO_WEEK_HIGH_CHANGE)))
            .jsonPath("$.fiftyTwoWeekLowChangePercent")
            .value(is(sameNumber(DEFAULT_FIFTY_TWO_WEEK_LOW_CHANGE_PERCENT)))
            .jsonPath("$.fiftyTwoWeekHighChangePercent")
            .value(is(sameNumber(DEFAULT_FIFTY_TWO_WEEK_HIGH_CHANGE_PERCENT)))
            .jsonPath("$.fiftyTwoWeekRange")
            .value(is(DEFAULT_FIFTY_TWO_WEEK_RANGE))
            .jsonPath("$.extendedChange")
            .value(is(sameNumber(DEFAULT_EXTENDED_CHANGE)))
            .jsonPath("$.extendedPercentChange")
            .value(is(sameNumber(DEFAULT_EXTENDED_PERCENT_CHANGE)))
            .jsonPath("$.extendedPrice")
            .value(is(sameNumber(DEFAULT_EXTENDED_PRICE)))
            .jsonPath("$.extendedTimestamp")
            .value(is(DEFAULT_EXTENDED_TIMESTAMP.intValue()))
            .jsonPath("$.updatedAt")
            .value(is(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    void getNonExistingStockStatistics() {
        // Get the stockStatistics
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingStockStatistics() throws Exception {
        // Initialize the database
        insertedStockStatistics = stockStatisticsRepository.save(stockStatistics).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockStatistics
        StockStatistics updatedStockStatistics = stockStatisticsRepository.findById(stockStatistics.getId()).block();
        updatedStockStatistics
            .symbol(UPDATED_SYMBOL)
            .averageVolume(UPDATED_AVERAGE_VOLUME)
            .rolling1dChange(UPDATED_ROLLING_1_D_CHANGE)
            .rolling7dChange(UPDATED_ROLLING_7_D_CHANGE)
            .rollingChange(UPDATED_ROLLING_CHANGE)
            .fiftyTwoWeekLow(UPDATED_FIFTY_TWO_WEEK_LOW)
            .fiftyTwoWeekHigh(UPDATED_FIFTY_TWO_WEEK_HIGH)
            .fiftyTwoWeekLowChange(UPDATED_FIFTY_TWO_WEEK_LOW_CHANGE)
            .fiftyTwoWeekHighChange(UPDATED_FIFTY_TWO_WEEK_HIGH_CHANGE)
            .fiftyTwoWeekLowChangePercent(UPDATED_FIFTY_TWO_WEEK_LOW_CHANGE_PERCENT)
            .fiftyTwoWeekHighChangePercent(UPDATED_FIFTY_TWO_WEEK_HIGH_CHANGE_PERCENT)
            .fiftyTwoWeekRange(UPDATED_FIFTY_TWO_WEEK_RANGE)
            .extendedChange(UPDATED_EXTENDED_CHANGE)
            .extendedPercentChange(UPDATED_EXTENDED_PERCENT_CHANGE)
            .extendedPrice(UPDATED_EXTENDED_PRICE)
            .extendedTimestamp(UPDATED_EXTENDED_TIMESTAMP)
            .updatedAt(UPDATED_UPDATED_AT);
        StockStatisticsDTO stockStatisticsDTO = stockStatisticsMapper.toDto(updatedStockStatistics);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, stockStatisticsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(stockStatisticsDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the StockStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStockStatisticsToMatchAllProperties(updatedStockStatistics);
    }

    @Test
    void putNonExistingStockStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockStatistics.setId(UUID.randomUUID().toString());

        // Create the StockStatistics
        StockStatisticsDTO stockStatisticsDTO = stockStatisticsMapper.toDto(stockStatistics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, stockStatisticsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(stockStatisticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the StockStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchStockStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockStatistics.setId(UUID.randomUUID().toString());

        // Create the StockStatistics
        StockStatisticsDTO stockStatisticsDTO = stockStatisticsMapper.toDto(stockStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(stockStatisticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the StockStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamStockStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockStatistics.setId(UUID.randomUUID().toString());

        // Create the StockStatistics
        StockStatisticsDTO stockStatisticsDTO = stockStatisticsMapper.toDto(stockStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(stockStatisticsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the StockStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateStockStatisticsWithPatch() throws Exception {
        // Initialize the database
        insertedStockStatistics = stockStatisticsRepository.save(stockStatistics).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockStatistics using partial update
        StockStatistics partialUpdatedStockStatistics = new StockStatistics();
        partialUpdatedStockStatistics.setId(stockStatistics.getId());

        partialUpdatedStockStatistics
            .symbol(UPDATED_SYMBOL)
            .averageVolume(UPDATED_AVERAGE_VOLUME)
            .fiftyTwoWeekHigh(UPDATED_FIFTY_TWO_WEEK_HIGH)
            .fiftyTwoWeekLowChange(UPDATED_FIFTY_TWO_WEEK_LOW_CHANGE)
            .fiftyTwoWeekHighChange(UPDATED_FIFTY_TWO_WEEK_HIGH_CHANGE)
            .fiftyTwoWeekHighChangePercent(UPDATED_FIFTY_TWO_WEEK_HIGH_CHANGE_PERCENT)
            .fiftyTwoWeekRange(UPDATED_FIFTY_TWO_WEEK_RANGE)
            .extendedTimestamp(UPDATED_EXTENDED_TIMESTAMP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedStockStatistics.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedStockStatistics))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the StockStatistics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStockStatisticsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStockStatistics, stockStatistics),
            getPersistedStockStatistics(stockStatistics)
        );
    }

    @Test
    void fullUpdateStockStatisticsWithPatch() throws Exception {
        // Initialize the database
        insertedStockStatistics = stockStatisticsRepository.save(stockStatistics).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stockStatistics using partial update
        StockStatistics partialUpdatedStockStatistics = new StockStatistics();
        partialUpdatedStockStatistics.setId(stockStatistics.getId());

        partialUpdatedStockStatistics
            .symbol(UPDATED_SYMBOL)
            .averageVolume(UPDATED_AVERAGE_VOLUME)
            .rolling1dChange(UPDATED_ROLLING_1_D_CHANGE)
            .rolling7dChange(UPDATED_ROLLING_7_D_CHANGE)
            .rollingChange(UPDATED_ROLLING_CHANGE)
            .fiftyTwoWeekLow(UPDATED_FIFTY_TWO_WEEK_LOW)
            .fiftyTwoWeekHigh(UPDATED_FIFTY_TWO_WEEK_HIGH)
            .fiftyTwoWeekLowChange(UPDATED_FIFTY_TWO_WEEK_LOW_CHANGE)
            .fiftyTwoWeekHighChange(UPDATED_FIFTY_TWO_WEEK_HIGH_CHANGE)
            .fiftyTwoWeekLowChangePercent(UPDATED_FIFTY_TWO_WEEK_LOW_CHANGE_PERCENT)
            .fiftyTwoWeekHighChangePercent(UPDATED_FIFTY_TWO_WEEK_HIGH_CHANGE_PERCENT)
            .fiftyTwoWeekRange(UPDATED_FIFTY_TWO_WEEK_RANGE)
            .extendedChange(UPDATED_EXTENDED_CHANGE)
            .extendedPercentChange(UPDATED_EXTENDED_PERCENT_CHANGE)
            .extendedPrice(UPDATED_EXTENDED_PRICE)
            .extendedTimestamp(UPDATED_EXTENDED_TIMESTAMP)
            .updatedAt(UPDATED_UPDATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedStockStatistics.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedStockStatistics))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the StockStatistics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStockStatisticsUpdatableFieldsEquals(
            partialUpdatedStockStatistics,
            getPersistedStockStatistics(partialUpdatedStockStatistics)
        );
    }

    @Test
    void patchNonExistingStockStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockStatistics.setId(UUID.randomUUID().toString());

        // Create the StockStatistics
        StockStatisticsDTO stockStatisticsDTO = stockStatisticsMapper.toDto(stockStatistics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, stockStatisticsDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(stockStatisticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the StockStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchStockStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockStatistics.setId(UUID.randomUUID().toString());

        // Create the StockStatistics
        StockStatisticsDTO stockStatisticsDTO = stockStatisticsMapper.toDto(stockStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(stockStatisticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the StockStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamStockStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stockStatistics.setId(UUID.randomUUID().toString());

        // Create the StockStatistics
        StockStatisticsDTO stockStatisticsDTO = stockStatisticsMapper.toDto(stockStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(stockStatisticsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the StockStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteStockStatistics() {
        // Initialize the database
        insertedStockStatistics = stockStatisticsRepository.save(stockStatistics).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the stockStatistics
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, stockStatistics.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return stockStatisticsRepository.count().block();
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

    protected StockStatistics getPersistedStockStatistics(StockStatistics stockStatistics) {
        return stockStatisticsRepository.findById(stockStatistics.getId()).block();
    }

    protected void assertPersistedStockStatisticsToMatchAllProperties(StockStatistics expectedStockStatistics) {
        assertStockStatisticsAllPropertiesEquals(expectedStockStatistics, getPersistedStockStatistics(expectedStockStatistics));
    }

    protected void assertPersistedStockStatisticsToMatchUpdatableProperties(StockStatistics expectedStockStatistics) {
        assertStockStatisticsAllUpdatablePropertiesEquals(expectedStockStatistics, getPersistedStockStatistics(expectedStockStatistics));
    }
}
