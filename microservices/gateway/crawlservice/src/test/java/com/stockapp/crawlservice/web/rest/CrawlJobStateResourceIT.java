package com.stockapp.crawlservice.web.rest;

import static com.stockapp.crawlservice.domain.CrawlJobStateAsserts.*;
import static com.stockapp.crawlservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.crawlservice.IntegrationTest;
import com.stockapp.crawlservice.domain.CrawlJobState;
import com.stockapp.crawlservice.domain.enumeration.JobStatus;
import com.stockapp.crawlservice.repository.CrawlJobStateRepository;
import com.stockapp.crawlservice.service.dto.CrawlJobStateDTO;
import com.stockapp.crawlservice.service.mapper.CrawlJobStateMapper;
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
 * Integration tests for the {@link CrawlJobStateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CrawlJobStateResourceIT {

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_SUCCESSFUL_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_SUCCESSFUL_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final JobStatus DEFAULT_LAST_SYNC_STATUS = JobStatus.RUNNING;
    private static final JobStatus UPDATED_LAST_SYNC_STATUS = JobStatus.SUCCEEDED;

    private static final String DEFAULT_ERROR_LOG = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_LOG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crawl-job-states";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CrawlJobStateRepository crawlJobStateRepository;

    @Autowired
    private CrawlJobStateMapper crawlJobStateMapper;

    @Autowired
    private WebTestClient webTestClient;

    private CrawlJobState crawlJobState;

    private CrawlJobState insertedCrawlJobState;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrawlJobState createEntity() {
        return new CrawlJobState()
            .symbol(DEFAULT_SYMBOL)
            .lastSuccessfulTimestamp(DEFAULT_LAST_SUCCESSFUL_TIMESTAMP)
            .lastSyncStatus(DEFAULT_LAST_SYNC_STATUS)
            .errorLog(DEFAULT_ERROR_LOG);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrawlJobState createUpdatedEntity() {
        return new CrawlJobState()
            .symbol(UPDATED_SYMBOL)
            .lastSuccessfulTimestamp(UPDATED_LAST_SUCCESSFUL_TIMESTAMP)
            .lastSyncStatus(UPDATED_LAST_SYNC_STATUS)
            .errorLog(UPDATED_ERROR_LOG);
    }

    @BeforeEach
    void initTest() {
        crawlJobState = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCrawlJobState != null) {
            crawlJobStateRepository.delete(insertedCrawlJobState).block();
            insertedCrawlJobState = null;
        }
    }

    @Test
    void createCrawlJobState() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CrawlJobState
        CrawlJobStateDTO crawlJobStateDTO = crawlJobStateMapper.toDto(crawlJobState);
        var returnedCrawlJobStateDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(crawlJobStateDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(CrawlJobStateDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the CrawlJobState in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCrawlJobState = crawlJobStateMapper.toEntity(returnedCrawlJobStateDTO);
        assertCrawlJobStateUpdatableFieldsEquals(returnedCrawlJobState, getPersistedCrawlJobState(returnedCrawlJobState));

        insertedCrawlJobState = returnedCrawlJobState;
    }

    @Test
    void createCrawlJobStateWithExistingId() throws Exception {
        // Create the CrawlJobState with an existing ID
        crawlJobState.setId("existing_id");
        CrawlJobStateDTO crawlJobStateDTO = crawlJobStateMapper.toDto(crawlJobState);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(crawlJobStateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CrawlJobState in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkSymbolIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        crawlJobState.setSymbol(null);

        // Create the CrawlJobState, which fails.
        CrawlJobStateDTO crawlJobStateDTO = crawlJobStateMapper.toDto(crawlJobState);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(crawlJobStateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllCrawlJobStates() {
        // Initialize the database
        insertedCrawlJobState = crawlJobStateRepository.save(crawlJobState).block();

        // Get all the crawlJobStateList
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
            .value(hasItem(crawlJobState.getId()))
            .jsonPath("$.[*].symbol")
            .value(hasItem(DEFAULT_SYMBOL))
            .jsonPath("$.[*].lastSuccessfulTimestamp")
            .value(hasItem(DEFAULT_LAST_SUCCESSFUL_TIMESTAMP.toString()))
            .jsonPath("$.[*].lastSyncStatus")
            .value(hasItem(DEFAULT_LAST_SYNC_STATUS.toString()))
            .jsonPath("$.[*].errorLog")
            .value(hasItem(DEFAULT_ERROR_LOG));
    }

    @Test
    void getCrawlJobState() {
        // Initialize the database
        insertedCrawlJobState = crawlJobStateRepository.save(crawlJobState).block();

        // Get the crawlJobState
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, crawlJobState.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(crawlJobState.getId()))
            .jsonPath("$.symbol")
            .value(is(DEFAULT_SYMBOL))
            .jsonPath("$.lastSuccessfulTimestamp")
            .value(is(DEFAULT_LAST_SUCCESSFUL_TIMESTAMP.toString()))
            .jsonPath("$.lastSyncStatus")
            .value(is(DEFAULT_LAST_SYNC_STATUS.toString()))
            .jsonPath("$.errorLog")
            .value(is(DEFAULT_ERROR_LOG));
    }

    @Test
    void getNonExistingCrawlJobState() {
        // Get the crawlJobState
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCrawlJobState() throws Exception {
        // Initialize the database
        insertedCrawlJobState = crawlJobStateRepository.save(crawlJobState).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the crawlJobState
        CrawlJobState updatedCrawlJobState = crawlJobStateRepository.findById(crawlJobState.getId()).block();
        updatedCrawlJobState
            .symbol(UPDATED_SYMBOL)
            .lastSuccessfulTimestamp(UPDATED_LAST_SUCCESSFUL_TIMESTAMP)
            .lastSyncStatus(UPDATED_LAST_SYNC_STATUS)
            .errorLog(UPDATED_ERROR_LOG);
        CrawlJobStateDTO crawlJobStateDTO = crawlJobStateMapper.toDto(updatedCrawlJobState);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, crawlJobStateDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(crawlJobStateDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CrawlJobState in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCrawlJobStateToMatchAllProperties(updatedCrawlJobState);
    }

    @Test
    void putNonExistingCrawlJobState() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        crawlJobState.setId(UUID.randomUUID().toString());

        // Create the CrawlJobState
        CrawlJobStateDTO crawlJobStateDTO = crawlJobStateMapper.toDto(crawlJobState);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, crawlJobStateDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(crawlJobStateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CrawlJobState in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCrawlJobState() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        crawlJobState.setId(UUID.randomUUID().toString());

        // Create the CrawlJobState
        CrawlJobStateDTO crawlJobStateDTO = crawlJobStateMapper.toDto(crawlJobState);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(crawlJobStateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CrawlJobState in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCrawlJobState() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        crawlJobState.setId(UUID.randomUUID().toString());

        // Create the CrawlJobState
        CrawlJobStateDTO crawlJobStateDTO = crawlJobStateMapper.toDto(crawlJobState);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(crawlJobStateDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CrawlJobState in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCrawlJobStateWithPatch() throws Exception {
        // Initialize the database
        insertedCrawlJobState = crawlJobStateRepository.save(crawlJobState).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the crawlJobState using partial update
        CrawlJobState partialUpdatedCrawlJobState = new CrawlJobState();
        partialUpdatedCrawlJobState.setId(crawlJobState.getId());

        partialUpdatedCrawlJobState.lastSuccessfulTimestamp(UPDATED_LAST_SUCCESSFUL_TIMESTAMP).errorLog(UPDATED_ERROR_LOG);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCrawlJobState.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCrawlJobState))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CrawlJobState in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCrawlJobStateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCrawlJobState, crawlJobState),
            getPersistedCrawlJobState(crawlJobState)
        );
    }

    @Test
    void fullUpdateCrawlJobStateWithPatch() throws Exception {
        // Initialize the database
        insertedCrawlJobState = crawlJobStateRepository.save(crawlJobState).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the crawlJobState using partial update
        CrawlJobState partialUpdatedCrawlJobState = new CrawlJobState();
        partialUpdatedCrawlJobState.setId(crawlJobState.getId());

        partialUpdatedCrawlJobState
            .symbol(UPDATED_SYMBOL)
            .lastSuccessfulTimestamp(UPDATED_LAST_SUCCESSFUL_TIMESTAMP)
            .lastSyncStatus(UPDATED_LAST_SYNC_STATUS)
            .errorLog(UPDATED_ERROR_LOG);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCrawlJobState.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCrawlJobState))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CrawlJobState in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCrawlJobStateUpdatableFieldsEquals(partialUpdatedCrawlJobState, getPersistedCrawlJobState(partialUpdatedCrawlJobState));
    }

    @Test
    void patchNonExistingCrawlJobState() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        crawlJobState.setId(UUID.randomUUID().toString());

        // Create the CrawlJobState
        CrawlJobStateDTO crawlJobStateDTO = crawlJobStateMapper.toDto(crawlJobState);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, crawlJobStateDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(crawlJobStateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CrawlJobState in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCrawlJobState() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        crawlJobState.setId(UUID.randomUUID().toString());

        // Create the CrawlJobState
        CrawlJobStateDTO crawlJobStateDTO = crawlJobStateMapper.toDto(crawlJobState);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(crawlJobStateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CrawlJobState in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCrawlJobState() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        crawlJobState.setId(UUID.randomUUID().toString());

        // Create the CrawlJobState
        CrawlJobStateDTO crawlJobStateDTO = crawlJobStateMapper.toDto(crawlJobState);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(crawlJobStateDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CrawlJobState in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCrawlJobState() {
        // Initialize the database
        insertedCrawlJobState = crawlJobStateRepository.save(crawlJobState).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the crawlJobState
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, crawlJobState.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return crawlJobStateRepository.count().block();
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

    protected CrawlJobState getPersistedCrawlJobState(CrawlJobState crawlJobState) {
        return crawlJobStateRepository.findById(crawlJobState.getId()).block();
    }

    protected void assertPersistedCrawlJobStateToMatchAllProperties(CrawlJobState expectedCrawlJobState) {
        assertCrawlJobStateAllPropertiesEquals(expectedCrawlJobState, getPersistedCrawlJobState(expectedCrawlJobState));
    }

    protected void assertPersistedCrawlJobStateToMatchUpdatableProperties(CrawlJobState expectedCrawlJobState) {
        assertCrawlJobStateAllUpdatablePropertiesEquals(expectedCrawlJobState, getPersistedCrawlJobState(expectedCrawlJobState));
    }
}
