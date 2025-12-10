package com.stockapp.userservice.web.rest;

import static com.stockapp.userservice.domain.LoginHistoryAsserts.*;
import static com.stockapp.userservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.userservice.IntegrationTest;
import com.stockapp.userservice.domain.LoginHistory;
import com.stockapp.userservice.domain.enumeration.DeviceType;
import com.stockapp.userservice.domain.enumeration.LoginMethod;
import com.stockapp.userservice.repository.LoginHistoryRepository;
import com.stockapp.userservice.service.LoginHistoryService;
import com.stockapp.userservice.service.dto.LoginHistoryDTO;
import com.stockapp.userservice.service.mapper.LoginHistoryMapper;
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
 * Integration tests for the {@link LoginHistoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LoginHistoryResourceIT {

    private static final Instant DEFAULT_LOGIN_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LOGIN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LoginMethod DEFAULT_LOGIN_METHOD = LoginMethod.PASSWORD;
    private static final LoginMethod UPDATED_LOGIN_METHOD = LoginMethod.TWO_FACTOR;

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_USER_AGENT = "AAAAAAAAAA";
    private static final String UPDATED_USER_AGENT = "BBBBBBBBBB";

    private static final DeviceType DEFAULT_DEVICE_TYPE = DeviceType.WEB;
    private static final DeviceType UPDATED_DEVICE_TYPE = DeviceType.MOBILE_IOS;

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SUCCESSFUL = false;
    private static final Boolean UPDATED_SUCCESSFUL = true;

    private static final String DEFAULT_FAILURE_REASON = "AAAAAAAAAA";
    private static final String UPDATED_FAILURE_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/login-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LoginHistoryRepository loginHistoryRepository;

    @Mock
    private LoginHistoryRepository loginHistoryRepositoryMock;

    @Autowired
    private LoginHistoryMapper loginHistoryMapper;

    @Mock
    private LoginHistoryService loginHistoryServiceMock;

    @Autowired
    private WebTestClient webTestClient;

    private LoginHistory loginHistory;

    private LoginHistory insertedLoginHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoginHistory createEntity() {
        return new LoginHistory()
            .loginTime(DEFAULT_LOGIN_TIME)
            .loginMethod(DEFAULT_LOGIN_METHOD)
            .ipAddress(DEFAULT_IP_ADDRESS)
            .userAgent(DEFAULT_USER_AGENT)
            .deviceType(DEFAULT_DEVICE_TYPE)
            .location(DEFAULT_LOCATION)
            .successful(DEFAULT_SUCCESSFUL)
            .failureReason(DEFAULT_FAILURE_REASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoginHistory createUpdatedEntity() {
        return new LoginHistory()
            .loginTime(UPDATED_LOGIN_TIME)
            .loginMethod(UPDATED_LOGIN_METHOD)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT)
            .deviceType(UPDATED_DEVICE_TYPE)
            .location(UPDATED_LOCATION)
            .successful(UPDATED_SUCCESSFUL)
            .failureReason(UPDATED_FAILURE_REASON);
    }

    @BeforeEach
    void initTest() {
        loginHistory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLoginHistory != null) {
            loginHistoryRepository.delete(insertedLoginHistory).block();
            insertedLoginHistory = null;
        }
    }

    @Test
    void createLoginHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LoginHistory
        LoginHistoryDTO loginHistoryDTO = loginHistoryMapper.toDto(loginHistory);
        var returnedLoginHistoryDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(loginHistoryDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(LoginHistoryDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the LoginHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLoginHistory = loginHistoryMapper.toEntity(returnedLoginHistoryDTO);
        assertLoginHistoryUpdatableFieldsEquals(returnedLoginHistory, getPersistedLoginHistory(returnedLoginHistory));

        insertedLoginHistory = returnedLoginHistory;
    }

    @Test
    void createLoginHistoryWithExistingId() throws Exception {
        // Create the LoginHistory with an existing ID
        loginHistory.setId("existing_id");
        LoginHistoryDTO loginHistoryDTO = loginHistoryMapper.toDto(loginHistory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(loginHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LoginHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkLoginTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        loginHistory.setLoginTime(null);

        // Create the LoginHistory, which fails.
        LoginHistoryDTO loginHistoryDTO = loginHistoryMapper.toDto(loginHistory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(loginHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkLoginMethodIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        loginHistory.setLoginMethod(null);

        // Create the LoginHistory, which fails.
        LoginHistoryDTO loginHistoryDTO = loginHistoryMapper.toDto(loginHistory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(loginHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkSuccessfulIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        loginHistory.setSuccessful(null);

        // Create the LoginHistory, which fails.
        LoginHistoryDTO loginHistoryDTO = loginHistoryMapper.toDto(loginHistory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(loginHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllLoginHistories() {
        // Initialize the database
        insertedLoginHistory = loginHistoryRepository.save(loginHistory).block();

        // Get all the loginHistoryList
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
            .value(hasItem(loginHistory.getId()))
            .jsonPath("$.[*].loginTime")
            .value(hasItem(DEFAULT_LOGIN_TIME.toString()))
            .jsonPath("$.[*].loginMethod")
            .value(hasItem(DEFAULT_LOGIN_METHOD.toString()))
            .jsonPath("$.[*].ipAddress")
            .value(hasItem(DEFAULT_IP_ADDRESS))
            .jsonPath("$.[*].userAgent")
            .value(hasItem(DEFAULT_USER_AGENT))
            .jsonPath("$.[*].deviceType")
            .value(hasItem(DEFAULT_DEVICE_TYPE.toString()))
            .jsonPath("$.[*].location")
            .value(hasItem(DEFAULT_LOCATION))
            .jsonPath("$.[*].successful")
            .value(hasItem(DEFAULT_SUCCESSFUL))
            .jsonPath("$.[*].failureReason")
            .value(hasItem(DEFAULT_FAILURE_REASON));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLoginHistoriesWithEagerRelationshipsIsEnabled() {
        when(loginHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(loginHistoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLoginHistoriesWithEagerRelationshipsIsNotEnabled() {
        when(loginHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(loginHistoryRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getLoginHistory() {
        // Initialize the database
        insertedLoginHistory = loginHistoryRepository.save(loginHistory).block();

        // Get the loginHistory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, loginHistory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(loginHistory.getId()))
            .jsonPath("$.loginTime")
            .value(is(DEFAULT_LOGIN_TIME.toString()))
            .jsonPath("$.loginMethod")
            .value(is(DEFAULT_LOGIN_METHOD.toString()))
            .jsonPath("$.ipAddress")
            .value(is(DEFAULT_IP_ADDRESS))
            .jsonPath("$.userAgent")
            .value(is(DEFAULT_USER_AGENT))
            .jsonPath("$.deviceType")
            .value(is(DEFAULT_DEVICE_TYPE.toString()))
            .jsonPath("$.location")
            .value(is(DEFAULT_LOCATION))
            .jsonPath("$.successful")
            .value(is(DEFAULT_SUCCESSFUL))
            .jsonPath("$.failureReason")
            .value(is(DEFAULT_FAILURE_REASON));
    }

    @Test
    void getNonExistingLoginHistory() {
        // Get the loginHistory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingLoginHistory() throws Exception {
        // Initialize the database
        insertedLoginHistory = loginHistoryRepository.save(loginHistory).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loginHistory
        LoginHistory updatedLoginHistory = loginHistoryRepository.findById(loginHistory.getId()).block();
        updatedLoginHistory
            .loginTime(UPDATED_LOGIN_TIME)
            .loginMethod(UPDATED_LOGIN_METHOD)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT)
            .deviceType(UPDATED_DEVICE_TYPE)
            .location(UPDATED_LOCATION)
            .successful(UPDATED_SUCCESSFUL)
            .failureReason(UPDATED_FAILURE_REASON);
        LoginHistoryDTO loginHistoryDTO = loginHistoryMapper.toDto(updatedLoginHistory);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, loginHistoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(loginHistoryDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the LoginHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLoginHistoryToMatchAllProperties(updatedLoginHistory);
    }

    @Test
    void putNonExistingLoginHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loginHistory.setId(UUID.randomUUID().toString());

        // Create the LoginHistory
        LoginHistoryDTO loginHistoryDTO = loginHistoryMapper.toDto(loginHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, loginHistoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(loginHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LoginHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLoginHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loginHistory.setId(UUID.randomUUID().toString());

        // Create the LoginHistory
        LoginHistoryDTO loginHistoryDTO = loginHistoryMapper.toDto(loginHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(loginHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LoginHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLoginHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loginHistory.setId(UUID.randomUUID().toString());

        // Create the LoginHistory
        LoginHistoryDTO loginHistoryDTO = loginHistoryMapper.toDto(loginHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(loginHistoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the LoginHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLoginHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedLoginHistory = loginHistoryRepository.save(loginHistory).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loginHistory using partial update
        LoginHistory partialUpdatedLoginHistory = new LoginHistory();
        partialUpdatedLoginHistory.setId(loginHistory.getId());

        partialUpdatedLoginHistory
            .loginTime(UPDATED_LOGIN_TIME)
            .loginMethod(UPDATED_LOGIN_METHOD)
            .userAgent(UPDATED_USER_AGENT)
            .location(UPDATED_LOCATION)
            .successful(UPDATED_SUCCESSFUL)
            .failureReason(UPDATED_FAILURE_REASON);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLoginHistory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedLoginHistory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the LoginHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLoginHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLoginHistory, loginHistory),
            getPersistedLoginHistory(loginHistory)
        );
    }

    @Test
    void fullUpdateLoginHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedLoginHistory = loginHistoryRepository.save(loginHistory).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loginHistory using partial update
        LoginHistory partialUpdatedLoginHistory = new LoginHistory();
        partialUpdatedLoginHistory.setId(loginHistory.getId());

        partialUpdatedLoginHistory
            .loginTime(UPDATED_LOGIN_TIME)
            .loginMethod(UPDATED_LOGIN_METHOD)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT)
            .deviceType(UPDATED_DEVICE_TYPE)
            .location(UPDATED_LOCATION)
            .successful(UPDATED_SUCCESSFUL)
            .failureReason(UPDATED_FAILURE_REASON);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLoginHistory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedLoginHistory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the LoginHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLoginHistoryUpdatableFieldsEquals(partialUpdatedLoginHistory, getPersistedLoginHistory(partialUpdatedLoginHistory));
    }

    @Test
    void patchNonExistingLoginHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loginHistory.setId(UUID.randomUUID().toString());

        // Create the LoginHistory
        LoginHistoryDTO loginHistoryDTO = loginHistoryMapper.toDto(loginHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, loginHistoryDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(loginHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LoginHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLoginHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loginHistory.setId(UUID.randomUUID().toString());

        // Create the LoginHistory
        LoginHistoryDTO loginHistoryDTO = loginHistoryMapper.toDto(loginHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(loginHistoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LoginHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLoginHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loginHistory.setId(UUID.randomUUID().toString());

        // Create the LoginHistory
        LoginHistoryDTO loginHistoryDTO = loginHistoryMapper.toDto(loginHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(loginHistoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the LoginHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLoginHistory() {
        // Initialize the database
        insertedLoginHistory = loginHistoryRepository.save(loginHistory).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the loginHistory
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, loginHistory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return loginHistoryRepository.count().block();
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

    protected LoginHistory getPersistedLoginHistory(LoginHistory loginHistory) {
        return loginHistoryRepository.findById(loginHistory.getId()).block();
    }

    protected void assertPersistedLoginHistoryToMatchAllProperties(LoginHistory expectedLoginHistory) {
        assertLoginHistoryAllPropertiesEquals(expectedLoginHistory, getPersistedLoginHistory(expectedLoginHistory));
    }

    protected void assertPersistedLoginHistoryToMatchUpdatableProperties(LoginHistory expectedLoginHistory) {
        assertLoginHistoryAllUpdatablePropertiesEquals(expectedLoginHistory, getPersistedLoginHistory(expectedLoginHistory));
    }
}
