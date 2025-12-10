package com.stockapp.stockservice.web.rest;

import static com.stockapp.stockservice.domain.HistoricalPriceAsserts.*;
import static com.stockapp.stockservice.web.rest.TestUtil.createUpdateProxyForBean;
import static com.stockapp.stockservice.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.stockservice.IntegrationTest;
import com.stockapp.stockservice.domain.HistoricalPrice;
import com.stockapp.stockservice.repository.HistoricalPriceRepository;
import com.stockapp.stockservice.service.HistoricalPriceService;
import com.stockapp.stockservice.service.dto.HistoricalPriceDTO;
import com.stockapp.stockservice.service.mapper.HistoricalPriceMapper;
import java.math.BigDecimal;
import java.util.UUID;
import java.time.Instant;
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
 * Integration tests for the {@link HistoricalPriceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class HistoricalPriceResourceIT {

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATETIME = Instant.parse("2020-01-01T00:00:00Z");
    private static final Instant UPDATED_DATETIME = Instant.parse("2020-01-02T00:00:00Z");

    private static final String DEFAULT_INTERVAL = "AAAAAAAAAA";
    private static final String UPDATED_INTERVAL = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/historical-prices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HistoricalPriceRepository historicalPriceRepository;

    @Mock
    private HistoricalPriceRepository historicalPriceRepositoryMock;

    @Autowired
    private HistoricalPriceMapper historicalPriceMapper;

    @Mock
    private HistoricalPriceService historicalPriceServiceMock;

    @Autowired
    private WebTestClient webTestClient;

    private HistoricalPrice historicalPrice;

    private HistoricalPrice insertedHistoricalPrice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoricalPrice createEntity() {
        return new HistoricalPrice()
                .symbol(DEFAULT_SYMBOL)
                .datetime(DEFAULT_DATETIME)
                .interval(DEFAULT_INTERVAL)
                .open(DEFAULT_OPEN)
                .high(DEFAULT_HIGH)
                .low(DEFAULT_LOW)
                .close(DEFAULT_CLOSE)
                .volume(DEFAULT_VOLUME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoricalPrice createUpdatedEntity() {
        return new HistoricalPrice()
                .symbol(UPDATED_SYMBOL)
                .datetime(UPDATED_DATETIME)
                .interval(UPDATED_INTERVAL)
                .open(UPDATED_OPEN)
                .high(UPDATED_HIGH)
                .low(UPDATED_LOW)
                .close(UPDATED_CLOSE)
                .volume(UPDATED_VOLUME);
    }

    @BeforeEach
    void initTest() {
        historicalPrice = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedHistoricalPrice != null) {
            historicalPriceRepository.delete(insertedHistoricalPrice).block();
            insertedHistoricalPrice = null;
        }
    }

    @Test
    void createHistoricalPrice() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HistoricalPrice
        HistoricalPriceDTO historicalPriceDTO = historicalPriceMapper.toDto(historicalPrice);
        var returnedHistoricalPriceDTO = webTestClient
                .post()
                .uri(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(om.writeValueAsBytes(historicalPriceDTO))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(HistoricalPriceDTO.class)
                .returnResult()
                .getResponseBody();

        // Validate the HistoricalPrice in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHistoricalPrice = historicalPriceMapper.toEntity(returnedHistoricalPriceDTO);
        assertHistoricalPriceUpdatableFieldsEquals(returnedHistoricalPrice,
                getPersistedHistoricalPrice(returnedHistoricalPrice));

        insertedHistoricalPrice = returnedHistoricalPrice;
    }

    @Test
    void createHistoricalPriceWithExistingId() throws Exception {
        // Create the HistoricalPrice with an existing ID
        historicalPrice.setId("existing_id");
        HistoricalPriceDTO historicalPriceDTO = historicalPriceMapper.toDto(historicalPrice);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
                .post()
                .uri(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(om.writeValueAsBytes(historicalPriceDTO))
                .exchange()
                .expectStatus()
                .isBadRequest();

        // Validate the HistoricalPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkSymbolIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historicalPrice.setSymbol(null);

        // Create the HistoricalPrice, which fails.
        HistoricalPriceDTO historicalPriceDTO = historicalPriceMapper.toDto(historicalPrice);

        webTestClient
                .post()
                .uri(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(om.writeValueAsBytes(historicalPriceDTO))
                .exchange()
                .expectStatus()
                .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDatetimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historicalPrice.setDatetime(null);

        // Create the HistoricalPrice, which fails.
        HistoricalPriceDTO historicalPriceDTO = historicalPriceMapper.toDto(historicalPrice);

        webTestClient
                .post()
                .uri(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(om.writeValueAsBytes(historicalPriceDTO))
                .exchange()
                .expectStatus()
                .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkIntervalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historicalPrice.setInterval(null);

        // Create the HistoricalPrice, which fails.
        HistoricalPriceDTO historicalPriceDTO = historicalPriceMapper.toDto(historicalPrice);

        webTestClient
                .post()
                .uri(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(om.writeValueAsBytes(historicalPriceDTO))
                .exchange()
                .expectStatus()
                .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkOpenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historicalPrice.setOpen(null);

        // Create the HistoricalPrice, which fails.
        HistoricalPriceDTO historicalPriceDTO = historicalPriceMapper.toDto(historicalPrice);

        webTestClient
                .post()
                .uri(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(om.writeValueAsBytes(historicalPriceDTO))
                .exchange()
                .expectStatus()
                .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkHighIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historicalPrice.setHigh(null);

        // Create the HistoricalPrice, which fails.
        HistoricalPriceDTO historicalPriceDTO = historicalPriceMapper.toDto(historicalPrice);

        webTestClient
                .post()
                .uri(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(om.writeValueAsBytes(historicalPriceDTO))
                .exchange()
                .expectStatus()
                .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkLowIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historicalPrice.setLow(null);

        // Create the HistoricalPrice, which fails.
        HistoricalPriceDTO historicalPriceDTO = historicalPriceMapper.toDto(historicalPrice);

        webTestClient
                .post()
                .uri(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(om.writeValueAsBytes(historicalPriceDTO))
                .exchange()
                .expectStatus()
                .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCloseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historicalPrice.setClose(null);

        // Create the HistoricalPrice, which fails.
        HistoricalPriceDTO historicalPriceDTO = historicalPriceMapper.toDto(historicalPrice);

        webTestClient
                .post()
                .uri(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(om.writeValueAsBytes(historicalPriceDTO))
                .exchange()
                .expectStatus()
                .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkVolumeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historicalPrice.setVolume(null);

        // Create the HistoricalPrice, which fails.
        HistoricalPriceDTO historicalPriceDTO = historicalPriceMapper.toDto(historicalPrice);

        webTestClient
                .post()
                .uri(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(om.writeValueAsBytes(historicalPriceDTO))
                .exchange()
                .expectStatus()
                .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllHistoricalPrices() {
        // Initialize the database
        insertedHistoricalPrice = historicalPriceRepository.save(historicalPrice).block();

        // Get all the historicalPriceList
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
                .value(hasItem(historicalPrice.getId()))
                .jsonPath("$.[*].symbol")
                .value(hasItem(DEFAULT_SYMBOL))
                .jsonPath("$.[*].datetime")
                .value(hasItem(DEFAULT_DATETIME))
                .jsonPath("$.[*].interval")
                .value(hasItem(DEFAULT_INTERVAL))
                .jsonPath("$.[*].open")
                .value(hasItem(sameNumber(DEFAULT_OPEN)))
                .jsonPath("$.[*].high")
                .value(hasItem(sameNumber(DEFAULT_HIGH)))
                .jsonPath("$.[*].low")
                .value(hasItem(sameNumber(DEFAULT_LOW)))
                .jsonPath("$.[*].close")
                .value(hasItem(sameNumber(DEFAULT_CLOSE)))
                .jsonPath("$.[*].volume")
                .value(hasItem(DEFAULT_VOLUME.intValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHistoricalPricesWithEagerRelationshipsIsEnabled() {
        when(historicalPriceServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(historicalPriceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHistoricalPricesWithEagerRelationshipsIsNotEnabled() {
        when(historicalPriceServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(historicalPriceRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getHistoricalPrice() {
        // Initialize the database
        insertedHistoricalPrice = historicalPriceRepository.save(historicalPrice).block();

        // Get the historicalPrice
        webTestClient
                .get()
                .uri(ENTITY_API_URL_ID, historicalPrice.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id")
                .value(is(historicalPrice.getId()))
                .jsonPath("$.symbol")
                .value(is(DEFAULT_SYMBOL))
                .jsonPath("$.datetime")
                .value(is(DEFAULT_DATETIME))
                .jsonPath("$.interval")
                .value(is(DEFAULT_INTERVAL))
                .jsonPath("$.open")
                .value(is(sameNumber(DEFAULT_OPEN)))
                .jsonPath("$.high")
                .value(is(sameNumber(DEFAULT_HIGH)))
                .jsonPath("$.low")
                .value(is(sameNumber(DEFAULT_LOW)))
                .jsonPath("$.close")
                .value(is(sameNumber(DEFAULT_CLOSE)))
                .jsonPath("$.volume")
                .value(is(DEFAULT_VOLUME.intValue()));
    }

    @Test
    void getNonExistingHistoricalPrice() {
        // Get the historicalPrice
        webTestClient
                .get()
                .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
                .accept(MediaType.APPLICATION_PROBLEM_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void putExistingHistoricalPrice() throws Exception {
        // Initialize the database
        insertedHistoricalPrice = historicalPriceRepository.save(historicalPrice).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historicalPrice
        HistoricalPrice updatedHistoricalPrice = historicalPriceRepository.findById(historicalPrice.getId()).block();
        updatedHistoricalPrice
                .symbol(UPDATED_SYMBOL)
                .datetime(UPDATED_DATETIME)
                .interval(UPDATED_INTERVAL)
                .open(UPDATED_OPEN)
                .high(UPDATED_HIGH)
                .low(UPDATED_LOW)
                .close(UPDATED_CLOSE)
                .volume(UPDATED_VOLUME);
        HistoricalPriceDTO historicalPriceDTO = historicalPriceMapper.toDto(updatedHistoricalPrice);

        webTestClient
                .put()
                .uri(ENTITY_API_URL_ID, historicalPriceDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(om.writeValueAsBytes(historicalPriceDTO))
                .exchange()
                .expectStatus()
                .isOk();

        // Validate the HistoricalPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHistoricalPriceToMatchAllProperties(updatedHistoricalPrice);
    }

    @Test
    void putNonExistingHistoricalPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historicalPrice.setId(UUID.randomUUID().toString());

        // Create the HistoricalPrice
        HistoricalPriceDTO historicalPriceDTO = historicalPriceMapper.toDto(historicalPrice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
                .put()
                .uri(ENTITY_API_URL_ID, historicalPriceDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(om.writeValueAsBytes(historicalPriceDTO))
                .exchange()
                .expectStatus()
                .isBadRequest();

        // Validate the HistoricalPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchHistoricalPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historicalPrice.setId(UUID.randomUUID().toString());

        // Create the HistoricalPrice
        HistoricalPriceDTO historicalPriceDTO = historicalPriceMapper.toDto(historicalPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
                .put()
                .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(om.writeValueAsBytes(historicalPriceDTO))
                .exchange()
                .expectStatus()
                .isBadRequest();

        // Validate the HistoricalPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamHistoricalPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historicalPrice.setId(UUID.randomUUID().toString());

        // Create the HistoricalPrice
        HistoricalPriceDTO historicalPriceDTO = historicalPriceMapper.toDto(historicalPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
                .put()
                .uri(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(om.writeValueAsBytes(historicalPriceDTO))
                .exchange()
                .expectStatus()
                .isEqualTo(405);

        // Validate the HistoricalPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateHistoricalPriceWithPatch() throws Exception {
        // Initialize the database
        insertedHistoricalPrice = historicalPriceRepository.save(historicalPrice).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historicalPrice using partial update
        HistoricalPrice partialUpdatedHistoricalPrice = new HistoricalPrice();
        partialUpdatedHistoricalPrice.setId(historicalPrice.getId());

        partialUpdatedHistoricalPrice.datetime(UPDATED_DATETIME).open(UPDATED_OPEN).high(UPDATED_HIGH)
                .volume(UPDATED_VOLUME);

        webTestClient
                .patch()
                .uri(ENTITY_API_URL_ID, partialUpdatedHistoricalPrice.getId())
                .contentType(MediaType.valueOf("application/merge-patch+json"))
                .bodyValue(om.writeValueAsBytes(partialUpdatedHistoricalPrice))
                .exchange()
                .expectStatus()
                .isOk();

        // Validate the HistoricalPrice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoricalPriceUpdatableFieldsEquals(
                createUpdateProxyForBean(partialUpdatedHistoricalPrice, historicalPrice),
                getPersistedHistoricalPrice(historicalPrice));
    }

    @Test
    void fullUpdateHistoricalPriceWithPatch() throws Exception {
        // Initialize the database
        insertedHistoricalPrice = historicalPriceRepository.save(historicalPrice).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historicalPrice using partial update
        HistoricalPrice partialUpdatedHistoricalPrice = new HistoricalPrice();
        partialUpdatedHistoricalPrice.setId(historicalPrice.getId());

        partialUpdatedHistoricalPrice
                .symbol(UPDATED_SYMBOL)
                .datetime(UPDATED_DATETIME)
                .interval(UPDATED_INTERVAL)
                .open(UPDATED_OPEN)
                .high(UPDATED_HIGH)
                .low(UPDATED_LOW)
                .close(UPDATED_CLOSE)
                .volume(UPDATED_VOLUME);

        webTestClient
                .patch()
                .uri(ENTITY_API_URL_ID, partialUpdatedHistoricalPrice.getId())
                .contentType(MediaType.valueOf("application/merge-patch+json"))
                .bodyValue(om.writeValueAsBytes(partialUpdatedHistoricalPrice))
                .exchange()
                .expectStatus()
                .isOk();

        // Validate the HistoricalPrice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoricalPriceUpdatableFieldsEquals(
                partialUpdatedHistoricalPrice,
                getPersistedHistoricalPrice(partialUpdatedHistoricalPrice));
    }

    @Test
    void patchNonExistingHistoricalPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historicalPrice.setId(UUID.randomUUID().toString());

        // Create the HistoricalPrice
        HistoricalPriceDTO historicalPriceDTO = historicalPriceMapper.toDto(historicalPrice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
                .patch()
                .uri(ENTITY_API_URL_ID, historicalPriceDTO.getId())
                .contentType(MediaType.valueOf("application/merge-patch+json"))
                .bodyValue(om.writeValueAsBytes(historicalPriceDTO))
                .exchange()
                .expectStatus()
                .isBadRequest();

        // Validate the HistoricalPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchHistoricalPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historicalPrice.setId(UUID.randomUUID().toString());

        // Create the HistoricalPrice
        HistoricalPriceDTO historicalPriceDTO = historicalPriceMapper.toDto(historicalPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
                .patch()
                .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                .contentType(MediaType.valueOf("application/merge-patch+json"))
                .bodyValue(om.writeValueAsBytes(historicalPriceDTO))
                .exchange()
                .expectStatus()
                .isBadRequest();

        // Validate the HistoricalPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamHistoricalPrice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historicalPrice.setId(UUID.randomUUID().toString());

        // Create the HistoricalPrice
        HistoricalPriceDTO historicalPriceDTO = historicalPriceMapper.toDto(historicalPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
                .patch()
                .uri(ENTITY_API_URL)
                .contentType(MediaType.valueOf("application/merge-patch+json"))
                .bodyValue(om.writeValueAsBytes(historicalPriceDTO))
                .exchange()
                .expectStatus()
                .isEqualTo(405);

        // Validate the HistoricalPrice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteHistoricalPrice() {
        // Initialize the database
        insertedHistoricalPrice = historicalPriceRepository.save(historicalPrice).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the historicalPrice
        webTestClient
                .delete()
                .uri(ENTITY_API_URL_ID, historicalPrice.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return historicalPriceRepository.count().block();
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

    protected HistoricalPrice getPersistedHistoricalPrice(HistoricalPrice historicalPrice) {
        return historicalPriceRepository.findById(historicalPrice.getId()).block();
    }

    protected void assertPersistedHistoricalPriceToMatchAllProperties(HistoricalPrice expectedHistoricalPrice) {
        assertHistoricalPriceAllPropertiesEquals(expectedHistoricalPrice,
                getPersistedHistoricalPrice(expectedHistoricalPrice));
    }

    protected void assertPersistedHistoricalPriceToMatchUpdatableProperties(HistoricalPrice expectedHistoricalPrice) {
        assertHistoricalPriceAllUpdatablePropertiesEquals(expectedHistoricalPrice,
                getPersistedHistoricalPrice(expectedHistoricalPrice));
    }
}
