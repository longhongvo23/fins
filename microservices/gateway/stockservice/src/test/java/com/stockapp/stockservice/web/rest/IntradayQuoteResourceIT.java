package com.stockapp.stockservice.web.rest;

import static com.stockapp.stockservice.domain.IntradayQuoteAsserts.*;
import static com.stockapp.stockservice.web.rest.TestUtil.createUpdateProxyForBean;
import static com.stockapp.stockservice.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.stockservice.IntegrationTest;
import com.stockapp.stockservice.domain.IntradayQuote;
import com.stockapp.stockservice.repository.IntradayQuoteRepository;
import com.stockapp.stockservice.service.dto.IntradayQuoteDTO;
import com.stockapp.stockservice.service.mapper.IntradayQuoteMapper;
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
 * Integration tests for the {@link IntradayQuoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class IntradayQuoteResourceIT {

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EXCHANGE = "AAAAAAAAAA";
    private static final String UPDATED_EXCHANGE = "BBBBBBBBBB";

    private static final String DEFAULT_MIC_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MIC_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_DATETIME = "AAAAAAAAAA";
    private static final String UPDATED_DATETIME = "BBBBBBBBBB";

    private static final Long DEFAULT_TIMESTAMP = 1L;
    private static final Long UPDATED_TIMESTAMP = 2L;

    private static final Long DEFAULT_LAST_QUOTE_AT = 1L;
    private static final Long UPDATED_LAST_QUOTE_AT = 2L;

    private static final BigDecimal DEFAULT_OPEN = new BigDecimal(0);
    private static final BigDecimal UPDATED_OPEN = new BigDecimal(1);

    private static final BigDecimal DEFAULT_HIGH = new BigDecimal(0);
    private static final BigDecimal UPDATED_HIGH = new BigDecimal(1);

    private static final BigDecimal DEFAULT_LOW = new BigDecimal(0);
    private static final BigDecimal UPDATED_LOW = new BigDecimal(1);

    private static final BigDecimal DEFAULT_CLOSE = new BigDecimal(0);
    private static final BigDecimal UPDATED_CLOSE = new BigDecimal(1);

    private static final Long DEFAULT_VOLUME = 0L;
    private static final Long UPDATED_VOLUME = 1L;

    private static final BigDecimal DEFAULT_PREVIOUS_CLOSE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PREVIOUS_CLOSE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_CHANGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CHANGE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PERCENT_CHANGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PERCENT_CHANGE = new BigDecimal(2);

    private static final Long DEFAULT_AVERAGE_VOLUME = 0L;
    private static final Long UPDATED_AVERAGE_VOLUME = 1L;

    private static final Boolean DEFAULT_IS_MARKET_OPEN = false;
    private static final Boolean UPDATED_IS_MARKET_OPEN = true;

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/intraday-quotes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IntradayQuoteRepository intradayQuoteRepository;

    @Autowired
    private IntradayQuoteMapper intradayQuoteMapper;

    @Autowired
    private WebTestClient webTestClient;

    private IntradayQuote intradayQuote;

    private IntradayQuote insertedIntradayQuote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IntradayQuote createEntity() {
        return new IntradayQuote()
            .symbol(DEFAULT_SYMBOL)
            .name(DEFAULT_NAME)
            .exchange(DEFAULT_EXCHANGE)
            .micCode(DEFAULT_MIC_CODE)
            .currency(DEFAULT_CURRENCY)
            .datetime(DEFAULT_DATETIME)
            .timestamp(DEFAULT_TIMESTAMP)
            .lastQuoteAt(DEFAULT_LAST_QUOTE_AT)
            .open(DEFAULT_OPEN)
            .high(DEFAULT_HIGH)
            .low(DEFAULT_LOW)
            .close(DEFAULT_CLOSE)
            .volume(DEFAULT_VOLUME)
            .previousClose(DEFAULT_PREVIOUS_CLOSE)
            .change(DEFAULT_CHANGE)
            .percentChange(DEFAULT_PERCENT_CHANGE)
            .averageVolume(DEFAULT_AVERAGE_VOLUME)
            .isMarketOpen(DEFAULT_IS_MARKET_OPEN)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IntradayQuote createUpdatedEntity() {
        return new IntradayQuote()
            .symbol(UPDATED_SYMBOL)
            .name(UPDATED_NAME)
            .exchange(UPDATED_EXCHANGE)
            .micCode(UPDATED_MIC_CODE)
            .currency(UPDATED_CURRENCY)
            .datetime(UPDATED_DATETIME)
            .timestamp(UPDATED_TIMESTAMP)
            .lastQuoteAt(UPDATED_LAST_QUOTE_AT)
            .open(UPDATED_OPEN)
            .high(UPDATED_HIGH)
            .low(UPDATED_LOW)
            .close(UPDATED_CLOSE)
            .volume(UPDATED_VOLUME)
            .previousClose(UPDATED_PREVIOUS_CLOSE)
            .change(UPDATED_CHANGE)
            .percentChange(UPDATED_PERCENT_CHANGE)
            .averageVolume(UPDATED_AVERAGE_VOLUME)
            .isMarketOpen(UPDATED_IS_MARKET_OPEN)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    void initTest() {
        intradayQuote = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIntradayQuote != null) {
            intradayQuoteRepository.delete(insertedIntradayQuote).block();
            insertedIntradayQuote = null;
        }
    }

    @Test
    void createIntradayQuote() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IntradayQuote
        IntradayQuoteDTO intradayQuoteDTO = intradayQuoteMapper.toDto(intradayQuote);
        var returnedIntradayQuoteDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(intradayQuoteDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(IntradayQuoteDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the IntradayQuote in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedIntradayQuote = intradayQuoteMapper.toEntity(returnedIntradayQuoteDTO);
        assertIntradayQuoteUpdatableFieldsEquals(returnedIntradayQuote, getPersistedIntradayQuote(returnedIntradayQuote));

        insertedIntradayQuote = returnedIntradayQuote;
    }

    @Test
    void createIntradayQuoteWithExistingId() throws Exception {
        // Create the IntradayQuote with an existing ID
        intradayQuote.setId("existing_id");
        IntradayQuoteDTO intradayQuoteDTO = intradayQuoteMapper.toDto(intradayQuote);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(intradayQuoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntradayQuote in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkSymbolIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        intradayQuote.setSymbol(null);

        // Create the IntradayQuote, which fails.
        IntradayQuoteDTO intradayQuoteDTO = intradayQuoteMapper.toDto(intradayQuote);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(intradayQuoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDatetimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        intradayQuote.setDatetime(null);

        // Create the IntradayQuote, which fails.
        IntradayQuoteDTO intradayQuoteDTO = intradayQuoteMapper.toDto(intradayQuote);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(intradayQuoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkTimestampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        intradayQuote.setTimestamp(null);

        // Create the IntradayQuote, which fails.
        IntradayQuoteDTO intradayQuoteDTO = intradayQuoteMapper.toDto(intradayQuote);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(intradayQuoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkOpenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        intradayQuote.setOpen(null);

        // Create the IntradayQuote, which fails.
        IntradayQuoteDTO intradayQuoteDTO = intradayQuoteMapper.toDto(intradayQuote);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(intradayQuoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkHighIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        intradayQuote.setHigh(null);

        // Create the IntradayQuote, which fails.
        IntradayQuoteDTO intradayQuoteDTO = intradayQuoteMapper.toDto(intradayQuote);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(intradayQuoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkLowIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        intradayQuote.setLow(null);

        // Create the IntradayQuote, which fails.
        IntradayQuoteDTO intradayQuoteDTO = intradayQuoteMapper.toDto(intradayQuote);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(intradayQuoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCloseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        intradayQuote.setClose(null);

        // Create the IntradayQuote, which fails.
        IntradayQuoteDTO intradayQuoteDTO = intradayQuoteMapper.toDto(intradayQuote);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(intradayQuoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkVolumeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        intradayQuote.setVolume(null);

        // Create the IntradayQuote, which fails.
        IntradayQuoteDTO intradayQuoteDTO = intradayQuoteMapper.toDto(intradayQuote);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(intradayQuoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        intradayQuote.setUpdatedAt(null);

        // Create the IntradayQuote, which fails.
        IntradayQuoteDTO intradayQuoteDTO = intradayQuoteMapper.toDto(intradayQuote);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(intradayQuoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllIntradayQuotes() {
        // Initialize the database
        insertedIntradayQuote = intradayQuoteRepository.save(intradayQuote).block();

        // Get all the intradayQuoteList
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
            .value(hasItem(intradayQuote.getId()))
            .jsonPath("$.[*].symbol")
            .value(hasItem(DEFAULT_SYMBOL))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].exchange")
            .value(hasItem(DEFAULT_EXCHANGE))
            .jsonPath("$.[*].micCode")
            .value(hasItem(DEFAULT_MIC_CODE))
            .jsonPath("$.[*].currency")
            .value(hasItem(DEFAULT_CURRENCY))
            .jsonPath("$.[*].datetime")
            .value(hasItem(DEFAULT_DATETIME))
            .jsonPath("$.[*].timestamp")
            .value(hasItem(DEFAULT_TIMESTAMP.intValue()))
            .jsonPath("$.[*].lastQuoteAt")
            .value(hasItem(DEFAULT_LAST_QUOTE_AT.intValue()))
            .jsonPath("$.[*].open")
            .value(hasItem(sameNumber(DEFAULT_OPEN)))
            .jsonPath("$.[*].high")
            .value(hasItem(sameNumber(DEFAULT_HIGH)))
            .jsonPath("$.[*].low")
            .value(hasItem(sameNumber(DEFAULT_LOW)))
            .jsonPath("$.[*].close")
            .value(hasItem(sameNumber(DEFAULT_CLOSE)))
            .jsonPath("$.[*].volume")
            .value(hasItem(DEFAULT_VOLUME.intValue()))
            .jsonPath("$.[*].previousClose")
            .value(hasItem(sameNumber(DEFAULT_PREVIOUS_CLOSE)))
            .jsonPath("$.[*].change")
            .value(hasItem(sameNumber(DEFAULT_CHANGE)))
            .jsonPath("$.[*].percentChange")
            .value(hasItem(sameNumber(DEFAULT_PERCENT_CHANGE)))
            .jsonPath("$.[*].averageVolume")
            .value(hasItem(DEFAULT_AVERAGE_VOLUME.intValue()))
            .jsonPath("$.[*].isMarketOpen")
            .value(hasItem(DEFAULT_IS_MARKET_OPEN))
            .jsonPath("$.[*].updatedAt")
            .value(hasItem(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    void getIntradayQuote() {
        // Initialize the database
        insertedIntradayQuote = intradayQuoteRepository.save(intradayQuote).block();

        // Get the intradayQuote
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, intradayQuote.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(intradayQuote.getId()))
            .jsonPath("$.symbol")
            .value(is(DEFAULT_SYMBOL))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.exchange")
            .value(is(DEFAULT_EXCHANGE))
            .jsonPath("$.micCode")
            .value(is(DEFAULT_MIC_CODE))
            .jsonPath("$.currency")
            .value(is(DEFAULT_CURRENCY))
            .jsonPath("$.datetime")
            .value(is(DEFAULT_DATETIME))
            .jsonPath("$.timestamp")
            .value(is(DEFAULT_TIMESTAMP.intValue()))
            .jsonPath("$.lastQuoteAt")
            .value(is(DEFAULT_LAST_QUOTE_AT.intValue()))
            .jsonPath("$.open")
            .value(is(sameNumber(DEFAULT_OPEN)))
            .jsonPath("$.high")
            .value(is(sameNumber(DEFAULT_HIGH)))
            .jsonPath("$.low")
            .value(is(sameNumber(DEFAULT_LOW)))
            .jsonPath("$.close")
            .value(is(sameNumber(DEFAULT_CLOSE)))
            .jsonPath("$.volume")
            .value(is(DEFAULT_VOLUME.intValue()))
            .jsonPath("$.previousClose")
            .value(is(sameNumber(DEFAULT_PREVIOUS_CLOSE)))
            .jsonPath("$.change")
            .value(is(sameNumber(DEFAULT_CHANGE)))
            .jsonPath("$.percentChange")
            .value(is(sameNumber(DEFAULT_PERCENT_CHANGE)))
            .jsonPath("$.averageVolume")
            .value(is(DEFAULT_AVERAGE_VOLUME.intValue()))
            .jsonPath("$.isMarketOpen")
            .value(is(DEFAULT_IS_MARKET_OPEN))
            .jsonPath("$.updatedAt")
            .value(is(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    void getNonExistingIntradayQuote() {
        // Get the intradayQuote
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingIntradayQuote() throws Exception {
        // Initialize the database
        insertedIntradayQuote = intradayQuoteRepository.save(intradayQuote).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the intradayQuote
        IntradayQuote updatedIntradayQuote = intradayQuoteRepository.findById(intradayQuote.getId()).block();
        updatedIntradayQuote
            .symbol(UPDATED_SYMBOL)
            .name(UPDATED_NAME)
            .exchange(UPDATED_EXCHANGE)
            .micCode(UPDATED_MIC_CODE)
            .currency(UPDATED_CURRENCY)
            .datetime(UPDATED_DATETIME)
            .timestamp(UPDATED_TIMESTAMP)
            .lastQuoteAt(UPDATED_LAST_QUOTE_AT)
            .open(UPDATED_OPEN)
            .high(UPDATED_HIGH)
            .low(UPDATED_LOW)
            .close(UPDATED_CLOSE)
            .volume(UPDATED_VOLUME)
            .previousClose(UPDATED_PREVIOUS_CLOSE)
            .change(UPDATED_CHANGE)
            .percentChange(UPDATED_PERCENT_CHANGE)
            .averageVolume(UPDATED_AVERAGE_VOLUME)
            .isMarketOpen(UPDATED_IS_MARKET_OPEN)
            .updatedAt(UPDATED_UPDATED_AT);
        IntradayQuoteDTO intradayQuoteDTO = intradayQuoteMapper.toDto(updatedIntradayQuote);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, intradayQuoteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(intradayQuoteDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IntradayQuote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIntradayQuoteToMatchAllProperties(updatedIntradayQuote);
    }

    @Test
    void putNonExistingIntradayQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        intradayQuote.setId(UUID.randomUUID().toString());

        // Create the IntradayQuote
        IntradayQuoteDTO intradayQuoteDTO = intradayQuoteMapper.toDto(intradayQuote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, intradayQuoteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(intradayQuoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntradayQuote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIntradayQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        intradayQuote.setId(UUID.randomUUID().toString());

        // Create the IntradayQuote
        IntradayQuoteDTO intradayQuoteDTO = intradayQuoteMapper.toDto(intradayQuote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(intradayQuoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntradayQuote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIntradayQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        intradayQuote.setId(UUID.randomUUID().toString());

        // Create the IntradayQuote
        IntradayQuoteDTO intradayQuoteDTO = intradayQuoteMapper.toDto(intradayQuote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(intradayQuoteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IntradayQuote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIntradayQuoteWithPatch() throws Exception {
        // Initialize the database
        insertedIntradayQuote = intradayQuoteRepository.save(intradayQuote).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the intradayQuote using partial update
        IntradayQuote partialUpdatedIntradayQuote = new IntradayQuote();
        partialUpdatedIntradayQuote.setId(intradayQuote.getId());

        partialUpdatedIntradayQuote
            .symbol(UPDATED_SYMBOL)
            .name(UPDATED_NAME)
            .timestamp(UPDATED_TIMESTAMP)
            .lastQuoteAt(UPDATED_LAST_QUOTE_AT)
            .open(UPDATED_OPEN)
            .low(UPDATED_LOW)
            .close(UPDATED_CLOSE)
            .volume(UPDATED_VOLUME)
            .previousClose(UPDATED_PREVIOUS_CLOSE)
            .change(UPDATED_CHANGE)
            .percentChange(UPDATED_PERCENT_CHANGE)
            .averageVolume(UPDATED_AVERAGE_VOLUME)
            .isMarketOpen(UPDATED_IS_MARKET_OPEN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIntradayQuote.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedIntradayQuote))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IntradayQuote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIntradayQuoteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIntradayQuote, intradayQuote),
            getPersistedIntradayQuote(intradayQuote)
        );
    }

    @Test
    void fullUpdateIntradayQuoteWithPatch() throws Exception {
        // Initialize the database
        insertedIntradayQuote = intradayQuoteRepository.save(intradayQuote).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the intradayQuote using partial update
        IntradayQuote partialUpdatedIntradayQuote = new IntradayQuote();
        partialUpdatedIntradayQuote.setId(intradayQuote.getId());

        partialUpdatedIntradayQuote
            .symbol(UPDATED_SYMBOL)
            .name(UPDATED_NAME)
            .exchange(UPDATED_EXCHANGE)
            .micCode(UPDATED_MIC_CODE)
            .currency(UPDATED_CURRENCY)
            .datetime(UPDATED_DATETIME)
            .timestamp(UPDATED_TIMESTAMP)
            .lastQuoteAt(UPDATED_LAST_QUOTE_AT)
            .open(UPDATED_OPEN)
            .high(UPDATED_HIGH)
            .low(UPDATED_LOW)
            .close(UPDATED_CLOSE)
            .volume(UPDATED_VOLUME)
            .previousClose(UPDATED_PREVIOUS_CLOSE)
            .change(UPDATED_CHANGE)
            .percentChange(UPDATED_PERCENT_CHANGE)
            .averageVolume(UPDATED_AVERAGE_VOLUME)
            .isMarketOpen(UPDATED_IS_MARKET_OPEN)
            .updatedAt(UPDATED_UPDATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIntradayQuote.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedIntradayQuote))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IntradayQuote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIntradayQuoteUpdatableFieldsEquals(partialUpdatedIntradayQuote, getPersistedIntradayQuote(partialUpdatedIntradayQuote));
    }

    @Test
    void patchNonExistingIntradayQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        intradayQuote.setId(UUID.randomUUID().toString());

        // Create the IntradayQuote
        IntradayQuoteDTO intradayQuoteDTO = intradayQuoteMapper.toDto(intradayQuote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, intradayQuoteDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(intradayQuoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntradayQuote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIntradayQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        intradayQuote.setId(UUID.randomUUID().toString());

        // Create the IntradayQuote
        IntradayQuoteDTO intradayQuoteDTO = intradayQuoteMapper.toDto(intradayQuote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(intradayQuoteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IntradayQuote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIntradayQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        intradayQuote.setId(UUID.randomUUID().toString());

        // Create the IntradayQuote
        IntradayQuoteDTO intradayQuoteDTO = intradayQuoteMapper.toDto(intradayQuote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(intradayQuoteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IntradayQuote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIntradayQuote() {
        // Initialize the database
        insertedIntradayQuote = intradayQuoteRepository.save(intradayQuote).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the intradayQuote
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, intradayQuote.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return intradayQuoteRepository.count().block();
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

    protected IntradayQuote getPersistedIntradayQuote(IntradayQuote intradayQuote) {
        return intradayQuoteRepository.findById(intradayQuote.getId()).block();
    }

    protected void assertPersistedIntradayQuoteToMatchAllProperties(IntradayQuote expectedIntradayQuote) {
        assertIntradayQuoteAllPropertiesEquals(expectedIntradayQuote, getPersistedIntradayQuote(expectedIntradayQuote));
    }

    protected void assertPersistedIntradayQuoteToMatchUpdatableProperties(IntradayQuote expectedIntradayQuote) {
        assertIntradayQuoteAllUpdatablePropertiesEquals(expectedIntradayQuote, getPersistedIntradayQuote(expectedIntradayQuote));
    }
}
