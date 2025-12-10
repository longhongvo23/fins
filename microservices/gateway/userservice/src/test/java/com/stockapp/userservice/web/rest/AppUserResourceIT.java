package com.stockapp.userservice.web.rest;

import static com.stockapp.userservice.domain.AppUserAsserts.*;
import static com.stockapp.userservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.userservice.IntegrationTest;
import com.stockapp.userservice.domain.AppUser;
import com.stockapp.userservice.domain.enumeration.AccountStatus;
import com.stockapp.userservice.repository.AppUserRepository;
import com.stockapp.userservice.service.AppUserService;
import com.stockapp.userservice.service.dto.AppUserDTO;
import com.stockapp.userservice.service.mapper.AppUserMapper;
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
 * Integration tests for the {@link AppUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AppUserResourceIT {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "Vv@M(-.By";
    private static final String UPDATED_EMAIL = "^@#wix$._";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final AccountStatus DEFAULT_ACCOUNT_STATUS = AccountStatus.ACTIVE;
    private static final AccountStatus UPDATED_ACCOUNT_STATUS = AccountStatus.INACTIVE;

    private static final Boolean DEFAULT_EMAIL_VERIFIED = false;
    private static final Boolean UPDATED_EMAIL_VERIFIED = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_LOGIN_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_LOGIN_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_PASSWORD_CHANGE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_PASSWORD_CHANGE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_FAILED_LOGIN_ATTEMPTS = 0;
    private static final Integer UPDATED_FAILED_LOGIN_ATTEMPTS = 1;

    private static final Instant DEFAULT_ACCOUNT_LOCKED_UNTIL = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACCOUNT_LOCKED_UNTIL = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PASSWORD_RESET_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD_RESET_TOKEN = "BBBBBBBBBB";

    private static final Instant DEFAULT_PASSWORD_RESET_TOKEN_EXPIRY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PASSWORD_RESET_TOKEN_EXPIRY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_EMAIL_VERIFICATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_VERIFICATION_TOKEN = "BBBBBBBBBB";

    private static final Instant DEFAULT_EMAIL_VERIFICATION_TOKEN_EXPIRY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EMAIL_VERIFICATION_TOKEN_EXPIRY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_TWO_FACTOR_ENABLED = false;
    private static final Boolean UPDATED_TWO_FACTOR_ENABLED = true;

    private static final String DEFAULT_TWO_FACTOR_SECRET = "AAAAAAAAAA";
    private static final String UPDATED_TWO_FACTOR_SECRET = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_LOGIN_IP = "AAAAAAAAAA";
    private static final String UPDATED_LAST_LOGIN_IP = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final String DEFAULT_TIMEZONE = "AAAAAAAAAA";
    private static final String UPDATED_TIMEZONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/app-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppUserRepository appUserRepository;

    @Mock
    private AppUserRepository appUserRepositoryMock;

    @Autowired
    private AppUserMapper appUserMapper;

    @Mock
    private AppUserService appUserServiceMock;

    @Autowired
    private WebTestClient webTestClient;

    private AppUser appUser;

    private AppUser insertedAppUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUser createEntity() {
        return new AppUser()
            .login(DEFAULT_LOGIN)
            .password(DEFAULT_PASSWORD)
            .email(DEFAULT_EMAIL)
            .activated(DEFAULT_ACTIVATED)
            .accountStatus(DEFAULT_ACCOUNT_STATUS)
            .emailVerified(DEFAULT_EMAIL_VERIFIED)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .lastLoginDate(DEFAULT_LAST_LOGIN_DATE)
            .lastPasswordChangeDate(DEFAULT_LAST_PASSWORD_CHANGE_DATE)
            .failedLoginAttempts(DEFAULT_FAILED_LOGIN_ATTEMPTS)
            .accountLockedUntil(DEFAULT_ACCOUNT_LOCKED_UNTIL)
            .passwordResetToken(DEFAULT_PASSWORD_RESET_TOKEN)
            .passwordResetTokenExpiry(DEFAULT_PASSWORD_RESET_TOKEN_EXPIRY)
            .emailVerificationToken(DEFAULT_EMAIL_VERIFICATION_TOKEN)
            .emailVerificationTokenExpiry(DEFAULT_EMAIL_VERIFICATION_TOKEN_EXPIRY)
            .twoFactorEnabled(DEFAULT_TWO_FACTOR_ENABLED)
            .twoFactorSecret(DEFAULT_TWO_FACTOR_SECRET)
            .lastLoginIp(DEFAULT_LAST_LOGIN_IP)
            .language(DEFAULT_LANGUAGE)
            .timezone(DEFAULT_TIMEZONE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUser createUpdatedEntity() {
        return new AppUser()
            .login(UPDATED_LOGIN)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .activated(UPDATED_ACTIVATED)
            .accountStatus(UPDATED_ACCOUNT_STATUS)
            .emailVerified(UPDATED_EMAIL_VERIFIED)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastLoginDate(UPDATED_LAST_LOGIN_DATE)
            .lastPasswordChangeDate(UPDATED_LAST_PASSWORD_CHANGE_DATE)
            .failedLoginAttempts(UPDATED_FAILED_LOGIN_ATTEMPTS)
            .accountLockedUntil(UPDATED_ACCOUNT_LOCKED_UNTIL)
            .passwordResetToken(UPDATED_PASSWORD_RESET_TOKEN)
            .passwordResetTokenExpiry(UPDATED_PASSWORD_RESET_TOKEN_EXPIRY)
            .emailVerificationToken(UPDATED_EMAIL_VERIFICATION_TOKEN)
            .emailVerificationTokenExpiry(UPDATED_EMAIL_VERIFICATION_TOKEN_EXPIRY)
            .twoFactorEnabled(UPDATED_TWO_FACTOR_ENABLED)
            .twoFactorSecret(UPDATED_TWO_FACTOR_SECRET)
            .lastLoginIp(UPDATED_LAST_LOGIN_IP)
            .language(UPDATED_LANGUAGE)
            .timezone(UPDATED_TIMEZONE);
    }

    @BeforeEach
    void initTest() {
        appUser = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAppUser != null) {
            appUserRepository.delete(insertedAppUser).block();
            insertedAppUser = null;
        }
    }

    @Test
    void createAppUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);
        var returnedAppUserDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appUserDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(AppUserDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the AppUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAppUser = appUserMapper.toEntity(returnedAppUserDTO);
        assertAppUserUpdatableFieldsEquals(returnedAppUser, getPersistedAppUser(returnedAppUser));

        insertedAppUser = returnedAppUser;
    }

    @Test
    void createAppUserWithExistingId() throws Exception {
        // Create the AppUser with an existing ID
        appUser.setId("existing_id");
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkLoginIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appUser.setLogin(null);

        // Create the AppUser, which fails.
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkPasswordIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appUser.setPassword(null);

        // Create the AppUser, which fails.
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appUser.setEmail(null);

        // Create the AppUser, which fails.
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkActivatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appUser.setActivated(null);

        // Create the AppUser, which fails.
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkAccountStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appUser.setAccountStatus(null);

        // Create the AppUser, which fails.
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailVerifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appUser.setEmailVerified(null);

        // Create the AppUser, which fails.
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appUser.setCreatedDate(null);

        // Create the AppUser, which fails.
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkTwoFactorEnabledIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appUser.setTwoFactorEnabled(null);

        // Create the AppUser, which fails.
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllAppUsers() {
        // Initialize the database
        insertedAppUser = appUserRepository.save(appUser).block();

        // Get all the appUserList
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
            .value(hasItem(appUser.getId()))
            .jsonPath("$.[*].login")
            .value(hasItem(DEFAULT_LOGIN))
            .jsonPath("$.[*].password")
            .value(hasItem(DEFAULT_PASSWORD))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].activated")
            .value(hasItem(DEFAULT_ACTIVATED))
            .jsonPath("$.[*].accountStatus")
            .value(hasItem(DEFAULT_ACCOUNT_STATUS.toString()))
            .jsonPath("$.[*].emailVerified")
            .value(hasItem(DEFAULT_EMAIL_VERIFIED))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].lastLoginDate")
            .value(hasItem(DEFAULT_LAST_LOGIN_DATE.toString()))
            .jsonPath("$.[*].lastPasswordChangeDate")
            .value(hasItem(DEFAULT_LAST_PASSWORD_CHANGE_DATE.toString()))
            .jsonPath("$.[*].failedLoginAttempts")
            .value(hasItem(DEFAULT_FAILED_LOGIN_ATTEMPTS))
            .jsonPath("$.[*].accountLockedUntil")
            .value(hasItem(DEFAULT_ACCOUNT_LOCKED_UNTIL.toString()))
            .jsonPath("$.[*].passwordResetToken")
            .value(hasItem(DEFAULT_PASSWORD_RESET_TOKEN))
            .jsonPath("$.[*].passwordResetTokenExpiry")
            .value(hasItem(DEFAULT_PASSWORD_RESET_TOKEN_EXPIRY.toString()))
            .jsonPath("$.[*].emailVerificationToken")
            .value(hasItem(DEFAULT_EMAIL_VERIFICATION_TOKEN))
            .jsonPath("$.[*].emailVerificationTokenExpiry")
            .value(hasItem(DEFAULT_EMAIL_VERIFICATION_TOKEN_EXPIRY.toString()))
            .jsonPath("$.[*].twoFactorEnabled")
            .value(hasItem(DEFAULT_TWO_FACTOR_ENABLED))
            .jsonPath("$.[*].twoFactorSecret")
            .value(hasItem(DEFAULT_TWO_FACTOR_SECRET))
            .jsonPath("$.[*].lastLoginIp")
            .value(hasItem(DEFAULT_LAST_LOGIN_IP))
            .jsonPath("$.[*].language")
            .value(hasItem(DEFAULT_LANGUAGE))
            .jsonPath("$.[*].timezone")
            .value(hasItem(DEFAULT_TIMEZONE));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAppUsersWithEagerRelationshipsIsEnabled() {
        when(appUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(appUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAppUsersWithEagerRelationshipsIsNotEnabled() {
        when(appUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(appUserRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getAppUser() {
        // Initialize the database
        insertedAppUser = appUserRepository.save(appUser).block();

        // Get the appUser
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, appUser.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(appUser.getId()))
            .jsonPath("$.login")
            .value(is(DEFAULT_LOGIN))
            .jsonPath("$.password")
            .value(is(DEFAULT_PASSWORD))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.activated")
            .value(is(DEFAULT_ACTIVATED))
            .jsonPath("$.accountStatus")
            .value(is(DEFAULT_ACCOUNT_STATUS.toString()))
            .jsonPath("$.emailVerified")
            .value(is(DEFAULT_EMAIL_VERIFIED))
            .jsonPath("$.createdDate")
            .value(is(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.lastModifiedDate")
            .value(is(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.lastLoginDate")
            .value(is(DEFAULT_LAST_LOGIN_DATE.toString()))
            .jsonPath("$.lastPasswordChangeDate")
            .value(is(DEFAULT_LAST_PASSWORD_CHANGE_DATE.toString()))
            .jsonPath("$.failedLoginAttempts")
            .value(is(DEFAULT_FAILED_LOGIN_ATTEMPTS))
            .jsonPath("$.accountLockedUntil")
            .value(is(DEFAULT_ACCOUNT_LOCKED_UNTIL.toString()))
            .jsonPath("$.passwordResetToken")
            .value(is(DEFAULT_PASSWORD_RESET_TOKEN))
            .jsonPath("$.passwordResetTokenExpiry")
            .value(is(DEFAULT_PASSWORD_RESET_TOKEN_EXPIRY.toString()))
            .jsonPath("$.emailVerificationToken")
            .value(is(DEFAULT_EMAIL_VERIFICATION_TOKEN))
            .jsonPath("$.emailVerificationTokenExpiry")
            .value(is(DEFAULT_EMAIL_VERIFICATION_TOKEN_EXPIRY.toString()))
            .jsonPath("$.twoFactorEnabled")
            .value(is(DEFAULT_TWO_FACTOR_ENABLED))
            .jsonPath("$.twoFactorSecret")
            .value(is(DEFAULT_TWO_FACTOR_SECRET))
            .jsonPath("$.lastLoginIp")
            .value(is(DEFAULT_LAST_LOGIN_IP))
            .jsonPath("$.language")
            .value(is(DEFAULT_LANGUAGE))
            .jsonPath("$.timezone")
            .value(is(DEFAULT_TIMEZONE));
    }

    @Test
    void getNonExistingAppUser() {
        // Get the appUser
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAppUser() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.save(appUser).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appUser
        AppUser updatedAppUser = appUserRepository.findById(appUser.getId()).block();
        updatedAppUser
            .login(UPDATED_LOGIN)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .activated(UPDATED_ACTIVATED)
            .accountStatus(UPDATED_ACCOUNT_STATUS)
            .emailVerified(UPDATED_EMAIL_VERIFIED)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastLoginDate(UPDATED_LAST_LOGIN_DATE)
            .lastPasswordChangeDate(UPDATED_LAST_PASSWORD_CHANGE_DATE)
            .failedLoginAttempts(UPDATED_FAILED_LOGIN_ATTEMPTS)
            .accountLockedUntil(UPDATED_ACCOUNT_LOCKED_UNTIL)
            .passwordResetToken(UPDATED_PASSWORD_RESET_TOKEN)
            .passwordResetTokenExpiry(UPDATED_PASSWORD_RESET_TOKEN_EXPIRY)
            .emailVerificationToken(UPDATED_EMAIL_VERIFICATION_TOKEN)
            .emailVerificationTokenExpiry(UPDATED_EMAIL_VERIFICATION_TOKEN_EXPIRY)
            .twoFactorEnabled(UPDATED_TWO_FACTOR_ENABLED)
            .twoFactorSecret(UPDATED_TWO_FACTOR_SECRET)
            .lastLoginIp(UPDATED_LAST_LOGIN_IP)
            .language(UPDATED_LANGUAGE)
            .timezone(UPDATED_TIMEZONE);
        AppUserDTO appUserDTO = appUserMapper.toDto(updatedAppUser);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, appUserDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appUserDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppUserToMatchAllProperties(updatedAppUser);
    }

    @Test
    void putNonExistingAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUser.setId(UUID.randomUUID().toString());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, appUserDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUser.setId(UUID.randomUUID().toString());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUser.setId(UUID.randomUUID().toString());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appUserDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAppUserWithPatch() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.save(appUser).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appUser using partial update
        AppUser partialUpdatedAppUser = new AppUser();
        partialUpdatedAppUser.setId(appUser.getId());

        partialUpdatedAppUser
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .emailVerified(UPDATED_EMAIL_VERIFIED)
            .lastPasswordChangeDate(UPDATED_LAST_PASSWORD_CHANGE_DATE)
            .passwordResetToken(UPDATED_PASSWORD_RESET_TOKEN)
            .passwordResetTokenExpiry(UPDATED_PASSWORD_RESET_TOKEN_EXPIRY)
            .emailVerificationToken(UPDATED_EMAIL_VERIFICATION_TOKEN)
            .twoFactorEnabled(UPDATED_TWO_FACTOR_ENABLED)
            .twoFactorSecret(UPDATED_TWO_FACTOR_SECRET)
            .lastLoginIp(UPDATED_LAST_LOGIN_IP)
            .language(UPDATED_LANGUAGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAppUser.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAppUser))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppUserUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAppUser, appUser), getPersistedAppUser(appUser));
    }

    @Test
    void fullUpdateAppUserWithPatch() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.save(appUser).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appUser using partial update
        AppUser partialUpdatedAppUser = new AppUser();
        partialUpdatedAppUser.setId(appUser.getId());

        partialUpdatedAppUser
            .login(UPDATED_LOGIN)
            .password(UPDATED_PASSWORD)
            .email(UPDATED_EMAIL)
            .activated(UPDATED_ACTIVATED)
            .accountStatus(UPDATED_ACCOUNT_STATUS)
            .emailVerified(UPDATED_EMAIL_VERIFIED)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastLoginDate(UPDATED_LAST_LOGIN_DATE)
            .lastPasswordChangeDate(UPDATED_LAST_PASSWORD_CHANGE_DATE)
            .failedLoginAttempts(UPDATED_FAILED_LOGIN_ATTEMPTS)
            .accountLockedUntil(UPDATED_ACCOUNT_LOCKED_UNTIL)
            .passwordResetToken(UPDATED_PASSWORD_RESET_TOKEN)
            .passwordResetTokenExpiry(UPDATED_PASSWORD_RESET_TOKEN_EXPIRY)
            .emailVerificationToken(UPDATED_EMAIL_VERIFICATION_TOKEN)
            .emailVerificationTokenExpiry(UPDATED_EMAIL_VERIFICATION_TOKEN_EXPIRY)
            .twoFactorEnabled(UPDATED_TWO_FACTOR_ENABLED)
            .twoFactorSecret(UPDATED_TWO_FACTOR_SECRET)
            .lastLoginIp(UPDATED_LAST_LOGIN_IP)
            .language(UPDATED_LANGUAGE)
            .timezone(UPDATED_TIMEZONE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAppUser.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAppUser))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppUserUpdatableFieldsEquals(partialUpdatedAppUser, getPersistedAppUser(partialUpdatedAppUser));
    }

    @Test
    void patchNonExistingAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUser.setId(UUID.randomUUID().toString());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, appUserDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(appUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUser.setId(UUID.randomUUID().toString());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(appUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUser.setId(UUID.randomUUID().toString());

        // Create the AppUser
        AppUserDTO appUserDTO = appUserMapper.toDto(appUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(appUserDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAppUser() {
        // Initialize the database
        insertedAppUser = appUserRepository.save(appUser).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the appUser
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, appUser.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appUserRepository.count().block();
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

    protected AppUser getPersistedAppUser(AppUser appUser) {
        return appUserRepository.findById(appUser.getId()).block();
    }

    protected void assertPersistedAppUserToMatchAllProperties(AppUser expectedAppUser) {
        assertAppUserAllPropertiesEquals(expectedAppUser, getPersistedAppUser(expectedAppUser));
    }

    protected void assertPersistedAppUserToMatchUpdatableProperties(AppUser expectedAppUser) {
        assertAppUserAllUpdatablePropertiesEquals(expectedAppUser, getPersistedAppUser(expectedAppUser));
    }
}
