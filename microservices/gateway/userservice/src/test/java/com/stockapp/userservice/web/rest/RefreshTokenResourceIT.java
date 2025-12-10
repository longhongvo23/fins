package com.stockapp.userservice.web.rest;

import static com.stockapp.userservice.domain.RefreshTokenAsserts.*;
import static com.stockapp.userservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.userservice.IntegrationTest;
import com.stockapp.userservice.domain.RefreshToken;
import com.stockapp.userservice.repository.RefreshTokenRepository;
import com.stockapp.userservice.service.RefreshTokenService;
import com.stockapp.userservice.service.dto.RefreshTokenDTO;
import com.stockapp.userservice.service.mapper.RefreshTokenMapper;
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
 * Integration tests for the {@link RefreshTokenResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RefreshTokenResourceIT {

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXPIRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_USED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_USED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_USER_AGENT = "AAAAAAAAAA";
    private static final String UPDATED_USER_AGENT = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_REVOKED = false;
    private static final Boolean UPDATED_REVOKED = true;

    private static final Instant DEFAULT_REVOKED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVOKED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REVOKED_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REVOKED_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_REPLACED_BY_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_REPLACED_BY_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/refresh-tokens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepositoryMock;

    @Autowired
    private RefreshTokenMapper refreshTokenMapper;

    @Mock
    private RefreshTokenService refreshTokenServiceMock;

    @Autowired
    private WebTestClient webTestClient;

    private RefreshToken refreshToken;

    private RefreshToken insertedRefreshToken;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RefreshToken createEntity() {
        return new RefreshToken()
            .token(DEFAULT_TOKEN)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .createdDate(DEFAULT_CREATED_DATE)
            .usedDate(DEFAULT_USED_DATE)
            .ipAddress(DEFAULT_IP_ADDRESS)
            .userAgent(DEFAULT_USER_AGENT)
            .deviceId(DEFAULT_DEVICE_ID)
            .revoked(DEFAULT_REVOKED)
            .revokedDate(DEFAULT_REVOKED_DATE)
            .revokedReason(DEFAULT_REVOKED_REASON)
            .replacedByToken(DEFAULT_REPLACED_BY_TOKEN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RefreshToken createUpdatedEntity() {
        return new RefreshToken()
            .token(UPDATED_TOKEN)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .usedDate(UPDATED_USED_DATE)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT)
            .deviceId(UPDATED_DEVICE_ID)
            .revoked(UPDATED_REVOKED)
            .revokedDate(UPDATED_REVOKED_DATE)
            .revokedReason(UPDATED_REVOKED_REASON)
            .replacedByToken(UPDATED_REPLACED_BY_TOKEN);
    }

    @BeforeEach
    void initTest() {
        refreshToken = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRefreshToken != null) {
            refreshTokenRepository.delete(insertedRefreshToken).block();
            insertedRefreshToken = null;
        }
    }

    @Test
    void createRefreshToken() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RefreshToken
        RefreshTokenDTO refreshTokenDTO = refreshTokenMapper.toDto(refreshToken);
        var returnedRefreshTokenDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(refreshTokenDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(RefreshTokenDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the RefreshToken in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRefreshToken = refreshTokenMapper.toEntity(returnedRefreshTokenDTO);
        assertRefreshTokenUpdatableFieldsEquals(returnedRefreshToken, getPersistedRefreshToken(returnedRefreshToken));

        insertedRefreshToken = returnedRefreshToken;
    }

    @Test
    void createRefreshTokenWithExistingId() throws Exception {
        // Create the RefreshToken with an existing ID
        refreshToken.setId("existing_id");
        RefreshTokenDTO refreshTokenDTO = refreshTokenMapper.toDto(refreshToken);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(refreshTokenDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RefreshToken in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTokenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        refreshToken.setToken(null);

        // Create the RefreshToken, which fails.
        RefreshTokenDTO refreshTokenDTO = refreshTokenMapper.toDto(refreshToken);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(refreshTokenDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkExpiryDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        refreshToken.setExpiryDate(null);

        // Create the RefreshToken, which fails.
        RefreshTokenDTO refreshTokenDTO = refreshTokenMapper.toDto(refreshToken);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(refreshTokenDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        refreshToken.setCreatedDate(null);

        // Create the RefreshToken, which fails.
        RefreshTokenDTO refreshTokenDTO = refreshTokenMapper.toDto(refreshToken);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(refreshTokenDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkRevokedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        refreshToken.setRevoked(null);

        // Create the RefreshToken, which fails.
        RefreshTokenDTO refreshTokenDTO = refreshTokenMapper.toDto(refreshToken);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(refreshTokenDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllRefreshTokens() {
        // Initialize the database
        insertedRefreshToken = refreshTokenRepository.save(refreshToken).block();

        // Get all the refreshTokenList
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
            .value(hasItem(refreshToken.getId()))
            .jsonPath("$.[*].token")
            .value(hasItem(DEFAULT_TOKEN))
            .jsonPath("$.[*].expiryDate")
            .value(hasItem(DEFAULT_EXPIRY_DATE.toString()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].usedDate")
            .value(hasItem(DEFAULT_USED_DATE.toString()))
            .jsonPath("$.[*].ipAddress")
            .value(hasItem(DEFAULT_IP_ADDRESS))
            .jsonPath("$.[*].userAgent")
            .value(hasItem(DEFAULT_USER_AGENT))
            .jsonPath("$.[*].deviceId")
            .value(hasItem(DEFAULT_DEVICE_ID))
            .jsonPath("$.[*].revoked")
            .value(hasItem(DEFAULT_REVOKED))
            .jsonPath("$.[*].revokedDate")
            .value(hasItem(DEFAULT_REVOKED_DATE.toString()))
            .jsonPath("$.[*].revokedReason")
            .value(hasItem(DEFAULT_REVOKED_REASON))
            .jsonPath("$.[*].replacedByToken")
            .value(hasItem(DEFAULT_REPLACED_BY_TOKEN));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRefreshTokensWithEagerRelationshipsIsEnabled() {
        when(refreshTokenServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(refreshTokenServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRefreshTokensWithEagerRelationshipsIsNotEnabled() {
        when(refreshTokenServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(refreshTokenRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getRefreshToken() {
        // Initialize the database
        insertedRefreshToken = refreshTokenRepository.save(refreshToken).block();

        // Get the refreshToken
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, refreshToken.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(refreshToken.getId()))
            .jsonPath("$.token")
            .value(is(DEFAULT_TOKEN))
            .jsonPath("$.expiryDate")
            .value(is(DEFAULT_EXPIRY_DATE.toString()))
            .jsonPath("$.createdDate")
            .value(is(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.usedDate")
            .value(is(DEFAULT_USED_DATE.toString()))
            .jsonPath("$.ipAddress")
            .value(is(DEFAULT_IP_ADDRESS))
            .jsonPath("$.userAgent")
            .value(is(DEFAULT_USER_AGENT))
            .jsonPath("$.deviceId")
            .value(is(DEFAULT_DEVICE_ID))
            .jsonPath("$.revoked")
            .value(is(DEFAULT_REVOKED))
            .jsonPath("$.revokedDate")
            .value(is(DEFAULT_REVOKED_DATE.toString()))
            .jsonPath("$.revokedReason")
            .value(is(DEFAULT_REVOKED_REASON))
            .jsonPath("$.replacedByToken")
            .value(is(DEFAULT_REPLACED_BY_TOKEN));
    }

    @Test
    void getNonExistingRefreshToken() {
        // Get the refreshToken
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingRefreshToken() throws Exception {
        // Initialize the database
        insertedRefreshToken = refreshTokenRepository.save(refreshToken).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the refreshToken
        RefreshToken updatedRefreshToken = refreshTokenRepository.findById(refreshToken.getId()).block();
        updatedRefreshToken
            .token(UPDATED_TOKEN)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .usedDate(UPDATED_USED_DATE)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT)
            .deviceId(UPDATED_DEVICE_ID)
            .revoked(UPDATED_REVOKED)
            .revokedDate(UPDATED_REVOKED_DATE)
            .revokedReason(UPDATED_REVOKED_REASON)
            .replacedByToken(UPDATED_REPLACED_BY_TOKEN);
        RefreshTokenDTO refreshTokenDTO = refreshTokenMapper.toDto(updatedRefreshToken);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, refreshTokenDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(refreshTokenDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RefreshToken in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRefreshTokenToMatchAllProperties(updatedRefreshToken);
    }

    @Test
    void putNonExistingRefreshToken() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        refreshToken.setId(UUID.randomUUID().toString());

        // Create the RefreshToken
        RefreshTokenDTO refreshTokenDTO = refreshTokenMapper.toDto(refreshToken);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, refreshTokenDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(refreshTokenDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RefreshToken in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRefreshToken() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        refreshToken.setId(UUID.randomUUID().toString());

        // Create the RefreshToken
        RefreshTokenDTO refreshTokenDTO = refreshTokenMapper.toDto(refreshToken);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(refreshTokenDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RefreshToken in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRefreshToken() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        refreshToken.setId(UUID.randomUUID().toString());

        // Create the RefreshToken
        RefreshTokenDTO refreshTokenDTO = refreshTokenMapper.toDto(refreshToken);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(refreshTokenDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RefreshToken in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRefreshTokenWithPatch() throws Exception {
        // Initialize the database
        insertedRefreshToken = refreshTokenRepository.save(refreshToken).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the refreshToken using partial update
        RefreshToken partialUpdatedRefreshToken = new RefreshToken();
        partialUpdatedRefreshToken.setId(refreshToken.getId());

        partialUpdatedRefreshToken
            .usedDate(UPDATED_USED_DATE)
            .deviceId(UPDATED_DEVICE_ID)
            .revoked(UPDATED_REVOKED)
            .revokedReason(UPDATED_REVOKED_REASON)
            .replacedByToken(UPDATED_REPLACED_BY_TOKEN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRefreshToken.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedRefreshToken))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RefreshToken in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRefreshTokenUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRefreshToken, refreshToken),
            getPersistedRefreshToken(refreshToken)
        );
    }

    @Test
    void fullUpdateRefreshTokenWithPatch() throws Exception {
        // Initialize the database
        insertedRefreshToken = refreshTokenRepository.save(refreshToken).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the refreshToken using partial update
        RefreshToken partialUpdatedRefreshToken = new RefreshToken();
        partialUpdatedRefreshToken.setId(refreshToken.getId());

        partialUpdatedRefreshToken
            .token(UPDATED_TOKEN)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .usedDate(UPDATED_USED_DATE)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT)
            .deviceId(UPDATED_DEVICE_ID)
            .revoked(UPDATED_REVOKED)
            .revokedDate(UPDATED_REVOKED_DATE)
            .revokedReason(UPDATED_REVOKED_REASON)
            .replacedByToken(UPDATED_REPLACED_BY_TOKEN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRefreshToken.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedRefreshToken))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RefreshToken in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRefreshTokenUpdatableFieldsEquals(partialUpdatedRefreshToken, getPersistedRefreshToken(partialUpdatedRefreshToken));
    }

    @Test
    void patchNonExistingRefreshToken() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        refreshToken.setId(UUID.randomUUID().toString());

        // Create the RefreshToken
        RefreshTokenDTO refreshTokenDTO = refreshTokenMapper.toDto(refreshToken);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, refreshTokenDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(refreshTokenDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RefreshToken in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRefreshToken() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        refreshToken.setId(UUID.randomUUID().toString());

        // Create the RefreshToken
        RefreshTokenDTO refreshTokenDTO = refreshTokenMapper.toDto(refreshToken);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(refreshTokenDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RefreshToken in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRefreshToken() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        refreshToken.setId(UUID.randomUUID().toString());

        // Create the RefreshToken
        RefreshTokenDTO refreshTokenDTO = refreshTokenMapper.toDto(refreshToken);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(refreshTokenDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RefreshToken in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRefreshToken() {
        // Initialize the database
        insertedRefreshToken = refreshTokenRepository.save(refreshToken).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the refreshToken
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, refreshToken.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return refreshTokenRepository.count().block();
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

    protected RefreshToken getPersistedRefreshToken(RefreshToken refreshToken) {
        return refreshTokenRepository.findById(refreshToken.getId()).block();
    }

    protected void assertPersistedRefreshTokenToMatchAllProperties(RefreshToken expectedRefreshToken) {
        assertRefreshTokenAllPropertiesEquals(expectedRefreshToken, getPersistedRefreshToken(expectedRefreshToken));
    }

    protected void assertPersistedRefreshTokenToMatchUpdatableProperties(RefreshToken expectedRefreshToken) {
        assertRefreshTokenAllUpdatablePropertiesEquals(expectedRefreshToken, getPersistedRefreshToken(expectedRefreshToken));
    }
}
