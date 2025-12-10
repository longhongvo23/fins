package com.stockapp.userservice.web.rest;

import static com.stockapp.userservice.domain.PasswordHistoryAsserts.*;
import static com.stockapp.userservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.userservice.IntegrationTest;
import com.stockapp.userservice.domain.PasswordHistory;
import com.stockapp.userservice.repository.PasswordHistoryRepository;
import com.stockapp.userservice.service.PasswordHistoryService;
import com.stockapp.userservice.service.dto.PasswordHistoryDTO;
import com.stockapp.userservice.service.mapper.PasswordHistoryMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PasswordHistoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PasswordHistoryResourceIT {

    private static final String DEFAULT_PASSWORD_HASH = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD_HASH = "BBBBBBBBBB";

    private static final Instant DEFAULT_CHANGED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CHANGED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CHANGED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CHANGED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_CHANGE_REASON = "AAAAAAAAAA";
    private static final String UPDATED_CHANGE_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/password-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PasswordHistoryRepository passwordHistoryRepository;

    @Mock
    private PasswordHistoryRepository passwordHistoryRepositoryMock;

    @Autowired
    private PasswordHistoryMapper passwordHistoryMapper;

    @Mock
    private PasswordHistoryService passwordHistoryServiceMock;

    @Autowired
    private WebTestClient webTestClient;

    private PasswordHistory passwordHistory;

    private PasswordHistory insertedPasswordHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PasswordHistory createEntity() {
        return new PasswordHistory()
            .passwordHash(DEFAULT_PASSWORD_HASH)
            .changedDate(DEFAULT_CHANGED_DATE)
            .changedBy(DEFAULT_CHANGED_BY)
            .changeReason(DEFAULT_CHANGE_REASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PasswordHistory createUpdatedEntity() {
        return new PasswordHistory()
            .passwordHash(UPDATED_PASSWORD_HASH)
            .changedDate(UPDATED_CHANGED_DATE)
            .changedBy(UPDATED_CHANGED_BY)
            .changeReason(UPDATED_CHANGE_REASON);
    }

    @BeforeEach
    void initTest() {
        passwordHistory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPasswordHistory != null) {
            passwordHistoryRepository.delete(insertedPasswordHistory).block();
            insertedPasswordHistory = null;
        }
    }

    @Test
    void createPasswordHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PasswordHistory
        PasswordHistoryDTO passwordHistoryDTO = passwordHistoryMapper.toDto(passwordHistory);
        var returnedPasswordHistoryDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(passwordHistoryDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(PasswordHistoryDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the PasswordHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPasswordHistory = passwordHistoryMapper.toEntity(returnedPasswordHistoryDTO);
        assertPasswordHistoryUpdatableFieldsEquals(returnedPasswordHistory, getPersistedPasswordHistory(returnedPasswordHistory));

        insertedPasswordHistory = returnedPasswordHistory;
    }

    @Test
    void createPasswordHistoryWithExistingId() throws Exception {
        // Create the PasswordHistory with an existing ID
        passwordHistory.setId("existing_id");
        PasswordHistoryDTO passwordHistoryDTO = passwordHistoryMapper.toDto(passwordHistory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(passwordHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PasswordHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkPasswordHashIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        passwordHistory.setPasswordHash(null);

        // Create the PasswordHistory, which fails.
        PasswordHistoryDTO passwordHistoryDTO = passwordHistoryMapper.toDto(passwordHistory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(passwordHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkChangedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        passwordHistory.setChangedDate(null);

        // Create the PasswordHistory, which fails.
        PasswordHistoryDTO passwordHistoryDTO = passwordHistoryMapper.toDto(passwordHistory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(passwordHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllPasswordHistories() {
        // Initialize the database
        insertedPasswordHistory = passwordHistoryRepository.save(passwordHistory).block();

        // Get all the passwordHistoryList
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
            .value(hasItem(passwordHistory.getId()))
            .jsonPath("$.[*].passwordHash")
            .value(hasItem(DEFAULT_PASSWORD_HASH))
            .jsonPath("$.[*].changedDate")
            .value(hasItem(DEFAULT_CHANGED_DATE.toString()))
            .jsonPath("$.[*].changedBy")
            .value(hasItem(DEFAULT_CHANGED_BY))
            .jsonPath("$.[*].changeReason")
            .value(hasItem(DEFAULT_CHANGE_REASON));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPasswordHistoriesWithEagerRelationshipsIsEnabled() {
        when(passwordHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(passwordHistoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPasswordHistoriesWithEagerRelationshipsIsNotEnabled() {
        when(passwordHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(passwordHistoryRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPasswordHistory() {
        // Initialize the database
        insertedPasswordHistory = passwordHistoryRepository.save(passwordHistory).block();

        // Get the passwordHistory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, passwordHistory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(passwordHistory.getId()))
            .jsonPath("$.passwordHash")
            .value(is(DEFAULT_PASSWORD_HASH))
            .jsonPath("$.changedDate")
            .value(is(DEFAULT_CHANGED_DATE.toString()))
            .jsonPath("$.changedBy")
            .value(is(DEFAULT_CHANGED_BY))
            .jsonPath("$.changeReason")
            .value(is(DEFAULT_CHANGE_REASON));
    }

    @Test
    void getNonExistingPasswordHistory() {
        // Get the passwordHistory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPasswordHistory() throws Exception {
        // Initialize the database
        insertedPasswordHistory = passwordHistoryRepository.save(passwordHistory).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the passwordHistory
        PasswordHistory updatedPasswordHistory = passwordHistoryRepository.findById(passwordHistory.getId()).block();
        updatedPasswordHistory
            .passwordHash(UPDATED_PASSWORD_HASH)
            .changedDate(UPDATED_CHANGED_DATE)
            .changedBy(UPDATED_CHANGED_BY)
            .changeReason(UPDATED_CHANGE_REASON);
        PasswordHistoryDTO passwordHistoryDTO = passwordHistoryMapper.toDto(updatedPasswordHistory);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, passwordHistoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(passwordHistoryDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PasswordHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPasswordHistoryToMatchAllProperties(updatedPasswordHistory);
    }

    @Test
    void putNonExistingPasswordHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passwordHistory.setId(UUID.randomUUID().toString());

        // Create the PasswordHistory
        PasswordHistoryDTO passwordHistoryDTO = passwordHistoryMapper.toDto(passwordHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, passwordHistoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(passwordHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PasswordHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPasswordHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passwordHistory.setId(UUID.randomUUID().toString());

        // Create the PasswordHistory
        PasswordHistoryDTO passwordHistoryDTO = passwordHistoryMapper.toDto(passwordHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(passwordHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PasswordHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPasswordHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passwordHistory.setId(UUID.randomUUID().toString());

        // Create the PasswordHistory
        PasswordHistoryDTO passwordHistoryDTO = passwordHistoryMapper.toDto(passwordHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(passwordHistoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PasswordHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePasswordHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedPasswordHistory = passwordHistoryRepository.save(passwordHistory).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the passwordHistory using partial update
        PasswordHistory partialUpdatedPasswordHistory = new PasswordHistory();
        partialUpdatedPasswordHistory.setId(passwordHistory.getId());

        partialUpdatedPasswordHistory
            .passwordHash(UPDATED_PASSWORD_HASH)
            .changedDate(UPDATED_CHANGED_DATE)
            .changeReason(UPDATED_CHANGE_REASON);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPasswordHistory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPasswordHistory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PasswordHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPasswordHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPasswordHistory, passwordHistory),
            getPersistedPasswordHistory(passwordHistory)
        );
    }

    @Test
    void fullUpdatePasswordHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedPasswordHistory = passwordHistoryRepository.save(passwordHistory).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the passwordHistory using partial update
        PasswordHistory partialUpdatedPasswordHistory = new PasswordHistory();
        partialUpdatedPasswordHistory.setId(passwordHistory.getId());

        partialUpdatedPasswordHistory
            .passwordHash(UPDATED_PASSWORD_HASH)
            .changedDate(UPDATED_CHANGED_DATE)
            .changedBy(UPDATED_CHANGED_BY)
            .changeReason(UPDATED_CHANGE_REASON);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPasswordHistory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPasswordHistory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PasswordHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPasswordHistoryUpdatableFieldsEquals(
            partialUpdatedPasswordHistory,
            getPersistedPasswordHistory(partialUpdatedPasswordHistory)
        );
    }

    @Test
    void patchNonExistingPasswordHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passwordHistory.setId(UUID.randomUUID().toString());

        // Create the PasswordHistory
        PasswordHistoryDTO passwordHistoryDTO = passwordHistoryMapper.toDto(passwordHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, passwordHistoryDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(passwordHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PasswordHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPasswordHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passwordHistory.setId(UUID.randomUUID().toString());

        // Create the PasswordHistory
        PasswordHistoryDTO passwordHistoryDTO = passwordHistoryMapper.toDto(passwordHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(passwordHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PasswordHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPasswordHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passwordHistory.setId(UUID.randomUUID().toString());

        // Create the PasswordHistory
        PasswordHistoryDTO passwordHistoryDTO = passwordHistoryMapper.toDto(passwordHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(passwordHistoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PasswordHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePasswordHistory() {
        // Initialize the database
        insertedPasswordHistory = passwordHistoryRepository.save(passwordHistory).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the passwordHistory
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, passwordHistory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return passwordHistoryRepository.count().block();
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

    protected PasswordHistory getPersistedPasswordHistory(PasswordHistory passwordHistory) {
        return passwordHistoryRepository.findById(passwordHistory.getId()).block();
    }

    protected void assertPersistedPasswordHistoryToMatchAllProperties(PasswordHistory expectedPasswordHistory) {
        assertPasswordHistoryAllPropertiesEquals(expectedPasswordHistory, getPersistedPasswordHistory(expectedPasswordHistory));
    }

    protected void assertPersistedPasswordHistoryToMatchUpdatableProperties(PasswordHistory expectedPasswordHistory) {
        assertPasswordHistoryAllUpdatablePropertiesEquals(expectedPasswordHistory, getPersistedPasswordHistory(expectedPasswordHistory));
    }
}
