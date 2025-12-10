package com.stockapp.userservice.web.rest;

import static com.stockapp.userservice.domain.SessionAsserts.*;
import static com.stockapp.userservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.userservice.IntegrationTest;
import com.stockapp.userservice.domain.Session;
import com.stockapp.userservice.domain.enumeration.DeviceType;
import com.stockapp.userservice.domain.enumeration.SessionStatus;
import com.stockapp.userservice.repository.SessionRepository;
import com.stockapp.userservice.service.SessionService;
import com.stockapp.userservice.service.dto.SessionDTO;
import com.stockapp.userservice.service.mapper.SessionMapper;
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
 * Integration tests for the {@link SessionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SessionResourceIT {

    private static final String DEFAULT_SESSION_ID = "AAAAAAAAAA";
    private static final String UPDATED_SESSION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_REFRESH_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_REFRESH_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_USER_AGENT = "AAAAAAAAAA";
    private static final String UPDATED_USER_AGENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_LOGIN_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LOGIN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRY_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRY_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_ACTIVITY_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_ACTIVITY_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_NAME = "BBBBBBBBBB";

    private static final DeviceType DEFAULT_DEVICE_TYPE = DeviceType.WEB;
    private static final DeviceType UPDATED_DEVICE_TYPE = DeviceType.MOBILE_IOS;

    private static final String DEFAULT_OS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OS_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_OS_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_BROWSER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BROWSER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BROWSER_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_BROWSER_VERSION = "BBBBBBBBBB";

    private static final SessionStatus DEFAULT_STATUS = SessionStatus.ACTIVE;
    private static final SessionStatus UPDATED_STATUS = SessionStatus.EXPIRED;

    private static final Boolean DEFAULT_IS_TRUSTED_DEVICE = false;
    private static final Boolean UPDATED_IS_TRUSTED_DEVICE = true;

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_LOGOUT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LOGOUT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_REVOKED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVOKED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REVOKED_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REVOKED_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sessions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SessionRepository sessionRepository;

    @Mock
    private SessionRepository sessionRepositoryMock;

    @Autowired
    private SessionMapper sessionMapper;

    @Mock
    private SessionService sessionServiceMock;

    @Autowired
    private WebTestClient webTestClient;

    private Session session;

    private Session insertedSession;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Session createEntity() {
        return new Session()
            .sessionId(DEFAULT_SESSION_ID)
            .token(DEFAULT_TOKEN)
            .refreshToken(DEFAULT_REFRESH_TOKEN)
            .ipAddress(DEFAULT_IP_ADDRESS)
            .userAgent(DEFAULT_USER_AGENT)
            .loginTime(DEFAULT_LOGIN_TIME)
            .expiryTime(DEFAULT_EXPIRY_TIME)
            .lastActivityTime(DEFAULT_LAST_ACTIVITY_TIME)
            .deviceId(DEFAULT_DEVICE_ID)
            .deviceName(DEFAULT_DEVICE_NAME)
            .deviceType(DEFAULT_DEVICE_TYPE)
            .osName(DEFAULT_OS_NAME)
            .osVersion(DEFAULT_OS_VERSION)
            .browserName(DEFAULT_BROWSER_NAME)
            .browserVersion(DEFAULT_BROWSER_VERSION)
            .status(DEFAULT_STATUS)
            .isTrustedDevice(DEFAULT_IS_TRUSTED_DEVICE)
            .location(DEFAULT_LOCATION)
            .logoutTime(DEFAULT_LOGOUT_TIME)
            .revokedAt(DEFAULT_REVOKED_AT)
            .revokedReason(DEFAULT_REVOKED_REASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Session createUpdatedEntity() {
        return new Session()
            .sessionId(UPDATED_SESSION_ID)
            .token(UPDATED_TOKEN)
            .refreshToken(UPDATED_REFRESH_TOKEN)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT)
            .loginTime(UPDATED_LOGIN_TIME)
            .expiryTime(UPDATED_EXPIRY_TIME)
            .lastActivityTime(UPDATED_LAST_ACTIVITY_TIME)
            .deviceId(UPDATED_DEVICE_ID)
            .deviceName(UPDATED_DEVICE_NAME)
            .deviceType(UPDATED_DEVICE_TYPE)
            .osName(UPDATED_OS_NAME)
            .osVersion(UPDATED_OS_VERSION)
            .browserName(UPDATED_BROWSER_NAME)
            .browserVersion(UPDATED_BROWSER_VERSION)
            .status(UPDATED_STATUS)
            .isTrustedDevice(UPDATED_IS_TRUSTED_DEVICE)
            .location(UPDATED_LOCATION)
            .logoutTime(UPDATED_LOGOUT_TIME)
            .revokedAt(UPDATED_REVOKED_AT)
            .revokedReason(UPDATED_REVOKED_REASON);
    }

    @BeforeEach
    void initTest() {
        session = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSession != null) {
            sessionRepository.delete(insertedSession).block();
            insertedSession = null;
        }
    }

    @Test
    void createSession() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Session
        SessionDTO sessionDTO = sessionMapper.toDto(session);
        var returnedSessionDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sessionDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(SessionDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Session in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSession = sessionMapper.toEntity(returnedSessionDTO);
        assertSessionUpdatableFieldsEquals(returnedSession, getPersistedSession(returnedSession));

        insertedSession = returnedSession;
    }

    @Test
    void createSessionWithExistingId() throws Exception {
        // Create the Session with an existing ID
        session.setId("existing_id");
        SessionDTO sessionDTO = sessionMapper.toDto(session);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sessionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Session in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkSessionIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        session.setSessionId(null);

        // Create the Session, which fails.
        SessionDTO sessionDTO = sessionMapper.toDto(session);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sessionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkTokenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        session.setToken(null);

        // Create the Session, which fails.
        SessionDTO sessionDTO = sessionMapper.toDto(session);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sessionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkLoginTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        session.setLoginTime(null);

        // Create the Session, which fails.
        SessionDTO sessionDTO = sessionMapper.toDto(session);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sessionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkExpiryTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        session.setExpiryTime(null);

        // Create the Session, which fails.
        SessionDTO sessionDTO = sessionMapper.toDto(session);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sessionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        session.setStatus(null);

        // Create the Session, which fails.
        SessionDTO sessionDTO = sessionMapper.toDto(session);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sessionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllSessions() {
        // Initialize the database
        insertedSession = sessionRepository.save(session).block();

        // Get all the sessionList
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
            .value(hasItem(session.getId()))
            .jsonPath("$.[*].sessionId")
            .value(hasItem(DEFAULT_SESSION_ID))
            .jsonPath("$.[*].token")
            .value(hasItem(DEFAULT_TOKEN))
            .jsonPath("$.[*].refreshToken")
            .value(hasItem(DEFAULT_REFRESH_TOKEN))
            .jsonPath("$.[*].ipAddress")
            .value(hasItem(DEFAULT_IP_ADDRESS))
            .jsonPath("$.[*].userAgent")
            .value(hasItem(DEFAULT_USER_AGENT))
            .jsonPath("$.[*].loginTime")
            .value(hasItem(DEFAULT_LOGIN_TIME.toString()))
            .jsonPath("$.[*].expiryTime")
            .value(hasItem(DEFAULT_EXPIRY_TIME.toString()))
            .jsonPath("$.[*].lastActivityTime")
            .value(hasItem(DEFAULT_LAST_ACTIVITY_TIME.toString()))
            .jsonPath("$.[*].deviceId")
            .value(hasItem(DEFAULT_DEVICE_ID))
            .jsonPath("$.[*].deviceName")
            .value(hasItem(DEFAULT_DEVICE_NAME))
            .jsonPath("$.[*].deviceType")
            .value(hasItem(DEFAULT_DEVICE_TYPE.toString()))
            .jsonPath("$.[*].osName")
            .value(hasItem(DEFAULT_OS_NAME))
            .jsonPath("$.[*].osVersion")
            .value(hasItem(DEFAULT_OS_VERSION))
            .jsonPath("$.[*].browserName")
            .value(hasItem(DEFAULT_BROWSER_NAME))
            .jsonPath("$.[*].browserVersion")
            .value(hasItem(DEFAULT_BROWSER_VERSION))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].isTrustedDevice")
            .value(hasItem(DEFAULT_IS_TRUSTED_DEVICE))
            .jsonPath("$.[*].location")
            .value(hasItem(DEFAULT_LOCATION))
            .jsonPath("$.[*].logoutTime")
            .value(hasItem(DEFAULT_LOGOUT_TIME.toString()))
            .jsonPath("$.[*].revokedAt")
            .value(hasItem(DEFAULT_REVOKED_AT.toString()))
            .jsonPath("$.[*].revokedReason")
            .value(hasItem(DEFAULT_REVOKED_REASON));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSessionsWithEagerRelationshipsIsEnabled() {
        when(sessionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(sessionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSessionsWithEagerRelationshipsIsNotEnabled() {
        when(sessionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(sessionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getSession() {
        // Initialize the database
        insertedSession = sessionRepository.save(session).block();

        // Get the session
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, session.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(session.getId()))
            .jsonPath("$.sessionId")
            .value(is(DEFAULT_SESSION_ID))
            .jsonPath("$.token")
            .value(is(DEFAULT_TOKEN))
            .jsonPath("$.refreshToken")
            .value(is(DEFAULT_REFRESH_TOKEN))
            .jsonPath("$.ipAddress")
            .value(is(DEFAULT_IP_ADDRESS))
            .jsonPath("$.userAgent")
            .value(is(DEFAULT_USER_AGENT))
            .jsonPath("$.loginTime")
            .value(is(DEFAULT_LOGIN_TIME.toString()))
            .jsonPath("$.expiryTime")
            .value(is(DEFAULT_EXPIRY_TIME.toString()))
            .jsonPath("$.lastActivityTime")
            .value(is(DEFAULT_LAST_ACTIVITY_TIME.toString()))
            .jsonPath("$.deviceId")
            .value(is(DEFAULT_DEVICE_ID))
            .jsonPath("$.deviceName")
            .value(is(DEFAULT_DEVICE_NAME))
            .jsonPath("$.deviceType")
            .value(is(DEFAULT_DEVICE_TYPE.toString()))
            .jsonPath("$.osName")
            .value(is(DEFAULT_OS_NAME))
            .jsonPath("$.osVersion")
            .value(is(DEFAULT_OS_VERSION))
            .jsonPath("$.browserName")
            .value(is(DEFAULT_BROWSER_NAME))
            .jsonPath("$.browserVersion")
            .value(is(DEFAULT_BROWSER_VERSION))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()))
            .jsonPath("$.isTrustedDevice")
            .value(is(DEFAULT_IS_TRUSTED_DEVICE))
            .jsonPath("$.location")
            .value(is(DEFAULT_LOCATION))
            .jsonPath("$.logoutTime")
            .value(is(DEFAULT_LOGOUT_TIME.toString()))
            .jsonPath("$.revokedAt")
            .value(is(DEFAULT_REVOKED_AT.toString()))
            .jsonPath("$.revokedReason")
            .value(is(DEFAULT_REVOKED_REASON));
    }

    @Test
    void getNonExistingSession() {
        // Get the session
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSession() throws Exception {
        // Initialize the database
        insertedSession = sessionRepository.save(session).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the session
        Session updatedSession = sessionRepository.findById(session.getId()).block();
        updatedSession
            .sessionId(UPDATED_SESSION_ID)
            .token(UPDATED_TOKEN)
            .refreshToken(UPDATED_REFRESH_TOKEN)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT)
            .loginTime(UPDATED_LOGIN_TIME)
            .expiryTime(UPDATED_EXPIRY_TIME)
            .lastActivityTime(UPDATED_LAST_ACTIVITY_TIME)
            .deviceId(UPDATED_DEVICE_ID)
            .deviceName(UPDATED_DEVICE_NAME)
            .deviceType(UPDATED_DEVICE_TYPE)
            .osName(UPDATED_OS_NAME)
            .osVersion(UPDATED_OS_VERSION)
            .browserName(UPDATED_BROWSER_NAME)
            .browserVersion(UPDATED_BROWSER_VERSION)
            .status(UPDATED_STATUS)
            .isTrustedDevice(UPDATED_IS_TRUSTED_DEVICE)
            .location(UPDATED_LOCATION)
            .logoutTime(UPDATED_LOGOUT_TIME)
            .revokedAt(UPDATED_REVOKED_AT)
            .revokedReason(UPDATED_REVOKED_REASON);
        SessionDTO sessionDTO = sessionMapper.toDto(updatedSession);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sessionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sessionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Session in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSessionToMatchAllProperties(updatedSession);
    }

    @Test
    void putNonExistingSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        session.setId(UUID.randomUUID().toString());

        // Create the Session
        SessionDTO sessionDTO = sessionMapper.toDto(session);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sessionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sessionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Session in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        session.setId(UUID.randomUUID().toString());

        // Create the Session
        SessionDTO sessionDTO = sessionMapper.toDto(session);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sessionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Session in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        session.setId(UUID.randomUUID().toString());

        // Create the Session
        SessionDTO sessionDTO = sessionMapper.toDto(session);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sessionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Session in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSessionWithPatch() throws Exception {
        // Initialize the database
        insertedSession = sessionRepository.save(session).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the session using partial update
        Session partialUpdatedSession = new Session();
        partialUpdatedSession.setId(session.getId());

        partialUpdatedSession
            .token(UPDATED_TOKEN)
            .refreshToken(UPDATED_REFRESH_TOKEN)
            .ipAddress(UPDATED_IP_ADDRESS)
            .loginTime(UPDATED_LOGIN_TIME)
            .expiryTime(UPDATED_EXPIRY_TIME)
            .deviceType(UPDATED_DEVICE_TYPE)
            .osName(UPDATED_OS_NAME)
            .browserName(UPDATED_BROWSER_NAME)
            .browserVersion(UPDATED_BROWSER_VERSION)
            .isTrustedDevice(UPDATED_IS_TRUSTED_DEVICE)
            .logoutTime(UPDATED_LOGOUT_TIME)
            .revokedReason(UPDATED_REVOKED_REASON);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSession.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSession))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Session in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSessionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSession, session), getPersistedSession(session));
    }

    @Test
    void fullUpdateSessionWithPatch() throws Exception {
        // Initialize the database
        insertedSession = sessionRepository.save(session).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the session using partial update
        Session partialUpdatedSession = new Session();
        partialUpdatedSession.setId(session.getId());

        partialUpdatedSession
            .sessionId(UPDATED_SESSION_ID)
            .token(UPDATED_TOKEN)
            .refreshToken(UPDATED_REFRESH_TOKEN)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT)
            .loginTime(UPDATED_LOGIN_TIME)
            .expiryTime(UPDATED_EXPIRY_TIME)
            .lastActivityTime(UPDATED_LAST_ACTIVITY_TIME)
            .deviceId(UPDATED_DEVICE_ID)
            .deviceName(UPDATED_DEVICE_NAME)
            .deviceType(UPDATED_DEVICE_TYPE)
            .osName(UPDATED_OS_NAME)
            .osVersion(UPDATED_OS_VERSION)
            .browserName(UPDATED_BROWSER_NAME)
            .browserVersion(UPDATED_BROWSER_VERSION)
            .status(UPDATED_STATUS)
            .isTrustedDevice(UPDATED_IS_TRUSTED_DEVICE)
            .location(UPDATED_LOCATION)
            .logoutTime(UPDATED_LOGOUT_TIME)
            .revokedAt(UPDATED_REVOKED_AT)
            .revokedReason(UPDATED_REVOKED_REASON);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSession.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSession))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Session in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSessionUpdatableFieldsEquals(partialUpdatedSession, getPersistedSession(partialUpdatedSession));
    }

    @Test
    void patchNonExistingSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        session.setId(UUID.randomUUID().toString());

        // Create the Session
        SessionDTO sessionDTO = sessionMapper.toDto(session);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, sessionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sessionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Session in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        session.setId(UUID.randomUUID().toString());

        // Create the Session
        SessionDTO sessionDTO = sessionMapper.toDto(session);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sessionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Session in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        session.setId(UUID.randomUUID().toString());

        // Create the Session
        SessionDTO sessionDTO = sessionMapper.toDto(session);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sessionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Session in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSession() {
        // Initialize the database
        insertedSession = sessionRepository.save(session).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the session
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, session.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sessionRepository.count().block();
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

    protected Session getPersistedSession(Session session) {
        return sessionRepository.findById(session.getId()).block();
    }

    protected void assertPersistedSessionToMatchAllProperties(Session expectedSession) {
        assertSessionAllPropertiesEquals(expectedSession, getPersistedSession(expectedSession));
    }

    protected void assertPersistedSessionToMatchUpdatableProperties(Session expectedSession) {
        assertSessionAllUpdatablePropertiesEquals(expectedSession, getPersistedSession(expectedSession));
    }
}
