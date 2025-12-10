package com.stockapp.userservice.web.rest;

import static com.stockapp.userservice.domain.UserProfileAsserts.*;
import static com.stockapp.userservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.userservice.IntegrationTest;
import com.stockapp.userservice.domain.UserProfile;
import com.stockapp.userservice.repository.UserProfileRepository;
import com.stockapp.userservice.service.UserProfileService;
import com.stockapp.userservice.service.dto.UserProfileDTO;
import com.stockapp.userservice.service.mapper.UserProfileMapper;
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
 * Integration tests for the {@link UserProfileResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserProfileResourceIT {

    private static final String DEFAULT_PHONE_NUMBER = "227553-2++3-4+2";
    private static final String UPDATED_PHONE_NUMBER = "+5-14326-3";

    private static final Boolean DEFAULT_PHONE_VERIFIED = false;
    private static final Boolean UPDATED_PHONE_VERIFIED = true;

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AVATAR_URL = "AAAAAAAAAA";
    private static final String UPDATED_AVATAR_URL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_BIO = "AAAAAAAAAA";
    private static final String UPDATED_BIO = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_VISIBILITY = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_VISIBILITY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SHOW_EMAIL = false;
    private static final Boolean UPDATED_SHOW_EMAIL = true;

    private static final Boolean DEFAULT_SHOW_PHONE = false;
    private static final Boolean UPDATED_SHOW_PHONE = true;

    private static final String ENTITY_API_URL = "/api/user-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserProfileRepository userProfileRepositoryMock;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Mock
    private UserProfileService userProfileServiceMock;

    @Autowired
    private WebTestClient webTestClient;

    private UserProfile userProfile;

    private UserProfile insertedUserProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createEntity() {
        return new UserProfile()
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .phoneVerified(DEFAULT_PHONE_VERIFIED)
            .country(DEFAULT_COUNTRY)
            .fullName(DEFAULT_FULL_NAME)
            .avatarUrl(DEFAULT_AVATAR_URL)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .bio(DEFAULT_BIO)
            .profileVisibility(DEFAULT_PROFILE_VISIBILITY)
            .showEmail(DEFAULT_SHOW_EMAIL)
            .showPhone(DEFAULT_SHOW_PHONE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createUpdatedEntity() {
        return new UserProfile()
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .phoneVerified(UPDATED_PHONE_VERIFIED)
            .country(UPDATED_COUNTRY)
            .fullName(UPDATED_FULL_NAME)
            .avatarUrl(UPDATED_AVATAR_URL)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .bio(UPDATED_BIO)
            .profileVisibility(UPDATED_PROFILE_VISIBILITY)
            .showEmail(UPDATED_SHOW_EMAIL)
            .showPhone(UPDATED_SHOW_PHONE);
    }

    @BeforeEach
    void initTest() {
        userProfile = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUserProfile != null) {
            userProfileRepository.delete(insertedUserProfile).block();
            insertedUserProfile = null;
        }
    }

    @Test
    void createUserProfile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
        var returnedUserProfileDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(UserProfileDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the UserProfile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserProfile = userProfileMapper.toEntity(returnedUserProfileDTO);
        assertUserProfileUpdatableFieldsEquals(returnedUserProfile, getPersistedUserProfile(returnedUserProfile));

        insertedUserProfile = returnedUserProfile;
    }

    @Test
    void createUserProfileWithExistingId() throws Exception {
        // Create the UserProfile with an existing ID
        userProfile.setId("existing_id");
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllUserProfiles() {
        // Initialize the database
        insertedUserProfile = userProfileRepository.save(userProfile).block();

        // Get all the userProfileList
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
            .value(hasItem(userProfile.getId()))
            .jsonPath("$.[*].phoneNumber")
            .value(hasItem(DEFAULT_PHONE_NUMBER))
            .jsonPath("$.[*].phoneVerified")
            .value(hasItem(DEFAULT_PHONE_VERIFIED))
            .jsonPath("$.[*].country")
            .value(hasItem(DEFAULT_COUNTRY))
            .jsonPath("$.[*].fullName")
            .value(hasItem(DEFAULT_FULL_NAME))
            .jsonPath("$.[*].avatarUrl")
            .value(hasItem(DEFAULT_AVATAR_URL))
            .jsonPath("$.[*].dateOfBirth")
            .value(hasItem(DEFAULT_DATE_OF_BIRTH.toString()))
            .jsonPath("$.[*].bio")
            .value(hasItem(DEFAULT_BIO))
            .jsonPath("$.[*].profileVisibility")
            .value(hasItem(DEFAULT_PROFILE_VISIBILITY))
            .jsonPath("$.[*].showEmail")
            .value(hasItem(DEFAULT_SHOW_EMAIL))
            .jsonPath("$.[*].showPhone")
            .value(hasItem(DEFAULT_SHOW_PHONE));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserProfilesWithEagerRelationshipsIsEnabled() {
        when(userProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(userProfileServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserProfilesWithEagerRelationshipsIsNotEnabled() {
        when(userProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(userProfileRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getUserProfile() {
        // Initialize the database
        insertedUserProfile = userProfileRepository.save(userProfile).block();

        // Get the userProfile
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userProfile.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userProfile.getId()))
            .jsonPath("$.phoneNumber")
            .value(is(DEFAULT_PHONE_NUMBER))
            .jsonPath("$.phoneVerified")
            .value(is(DEFAULT_PHONE_VERIFIED))
            .jsonPath("$.country")
            .value(is(DEFAULT_COUNTRY))
            .jsonPath("$.fullName")
            .value(is(DEFAULT_FULL_NAME))
            .jsonPath("$.avatarUrl")
            .value(is(DEFAULT_AVATAR_URL))
            .jsonPath("$.dateOfBirth")
            .value(is(DEFAULT_DATE_OF_BIRTH.toString()))
            .jsonPath("$.bio")
            .value(is(DEFAULT_BIO))
            .jsonPath("$.profileVisibility")
            .value(is(DEFAULT_PROFILE_VISIBILITY))
            .jsonPath("$.showEmail")
            .value(is(DEFAULT_SHOW_EMAIL))
            .jsonPath("$.showPhone")
            .value(is(DEFAULT_SHOW_PHONE));
    }

    @Test
    void getNonExistingUserProfile() {
        // Get the userProfile
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserProfile() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.save(userProfile).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userProfile
        UserProfile updatedUserProfile = userProfileRepository.findById(userProfile.getId()).block();
        updatedUserProfile
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .phoneVerified(UPDATED_PHONE_VERIFIED)
            .country(UPDATED_COUNTRY)
            .fullName(UPDATED_FULL_NAME)
            .avatarUrl(UPDATED_AVATAR_URL)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .bio(UPDATED_BIO)
            .profileVisibility(UPDATED_PROFILE_VISIBILITY)
            .showEmail(UPDATED_SHOW_EMAIL)
            .showPhone(UPDATED_SHOW_PHONE);
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(updatedUserProfile);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userProfileDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserProfileToMatchAllProperties(updatedUserProfile);
    }

    @Test
    void putNonExistingUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(UUID.randomUUID().toString());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userProfileDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(UUID.randomUUID().toString());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(UUID.randomUUID().toString());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUserProfileWithPatch() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.save(userProfile).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userProfile using partial update
        UserProfile partialUpdatedUserProfile = new UserProfile();
        partialUpdatedUserProfile.setId(userProfile.getId());

        partialUpdatedUserProfile
            .phoneVerified(UPDATED_PHONE_VERIFIED)
            .country(UPDATED_COUNTRY)
            .fullName(UPDATED_FULL_NAME)
            .avatarUrl(UPDATED_AVATAR_URL)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .bio(UPDATED_BIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserProfile.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUserProfile))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserProfileUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserProfile, userProfile),
            getPersistedUserProfile(userProfile)
        );
    }

    @Test
    void fullUpdateUserProfileWithPatch() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.save(userProfile).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userProfile using partial update
        UserProfile partialUpdatedUserProfile = new UserProfile();
        partialUpdatedUserProfile.setId(userProfile.getId());

        partialUpdatedUserProfile
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .phoneVerified(UPDATED_PHONE_VERIFIED)
            .country(UPDATED_COUNTRY)
            .fullName(UPDATED_FULL_NAME)
            .avatarUrl(UPDATED_AVATAR_URL)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .bio(UPDATED_BIO)
            .profileVisibility(UPDATED_PROFILE_VISIBILITY)
            .showEmail(UPDATED_SHOW_EMAIL)
            .showPhone(UPDATED_SHOW_PHONE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserProfile.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUserProfile))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserProfileUpdatableFieldsEquals(partialUpdatedUserProfile, getPersistedUserProfile(partialUpdatedUserProfile));
    }

    @Test
    void patchNonExistingUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(UUID.randomUUID().toString());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userProfileDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(UUID.randomUUID().toString());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(UUID.randomUUID().toString());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUserProfile() {
        // Initialize the database
        insertedUserProfile = userProfileRepository.save(userProfile).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userProfile
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userProfile.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userProfileRepository.count().block();
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

    protected UserProfile getPersistedUserProfile(UserProfile userProfile) {
        return userProfileRepository.findById(userProfile.getId()).block();
    }

    protected void assertPersistedUserProfileToMatchAllProperties(UserProfile expectedUserProfile) {
        assertUserProfileAllPropertiesEquals(expectedUserProfile, getPersistedUserProfile(expectedUserProfile));
    }

    protected void assertPersistedUserProfileToMatchUpdatableProperties(UserProfile expectedUserProfile) {
        assertUserProfileAllUpdatablePropertiesEquals(expectedUserProfile, getPersistedUserProfile(expectedUserProfile));
    }
}
