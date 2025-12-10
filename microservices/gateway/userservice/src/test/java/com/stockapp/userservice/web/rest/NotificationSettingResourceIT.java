package com.stockapp.userservice.web.rest;

import static com.stockapp.userservice.domain.NotificationSettingAsserts.*;
import static com.stockapp.userservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.userservice.IntegrationTest;
import com.stockapp.userservice.domain.NotificationSetting;
import com.stockapp.userservice.repository.NotificationSettingRepository;
import com.stockapp.userservice.service.NotificationSettingService;
import com.stockapp.userservice.service.dto.NotificationSettingDTO;
import com.stockapp.userservice.service.mapper.NotificationSettingMapper;
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
 * Integration tests for the {@link NotificationSettingResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class NotificationSettingResourceIT {

    private static final Boolean DEFAULT_EMAIL_ENABLED = false;
    private static final Boolean UPDATED_EMAIL_ENABLED = true;

    private static final Boolean DEFAULT_SMS_ENABLED = false;
    private static final Boolean UPDATED_SMS_ENABLED = true;

    private static final Boolean DEFAULT_PUSH_ENABLED = false;
    private static final Boolean UPDATED_PUSH_ENABLED = true;

    private static final Boolean DEFAULT_IN_APP_ENABLED = false;
    private static final Boolean UPDATED_IN_APP_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/notification-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NotificationSettingRepository notificationSettingRepository;

    @Mock
    private NotificationSettingRepository notificationSettingRepositoryMock;

    @Autowired
    private NotificationSettingMapper notificationSettingMapper;

    @Mock
    private NotificationSettingService notificationSettingServiceMock;

    @Autowired
    private WebTestClient webTestClient;

    private NotificationSetting notificationSetting;

    private NotificationSetting insertedNotificationSetting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationSetting createEntity() {
        return new NotificationSetting()
            .emailEnabled(DEFAULT_EMAIL_ENABLED)
            .smsEnabled(DEFAULT_SMS_ENABLED)
            .pushEnabled(DEFAULT_PUSH_ENABLED)
            .inAppEnabled(DEFAULT_IN_APP_ENABLED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationSetting createUpdatedEntity() {
        return new NotificationSetting()
            .emailEnabled(UPDATED_EMAIL_ENABLED)
            .smsEnabled(UPDATED_SMS_ENABLED)
            .pushEnabled(UPDATED_PUSH_ENABLED)
            .inAppEnabled(UPDATED_IN_APP_ENABLED);
    }

    @BeforeEach
    void initTest() {
        notificationSetting = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedNotificationSetting != null) {
            notificationSettingRepository.delete(insertedNotificationSetting).block();
            insertedNotificationSetting = null;
        }
    }

    @Test
    void createNotificationSetting() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NotificationSetting
        NotificationSettingDTO notificationSettingDTO = notificationSettingMapper.toDto(notificationSetting);
        var returnedNotificationSettingDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationSettingDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(NotificationSettingDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the NotificationSetting in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNotificationSetting = notificationSettingMapper.toEntity(returnedNotificationSettingDTO);
        assertNotificationSettingUpdatableFieldsEquals(
            returnedNotificationSetting,
            getPersistedNotificationSetting(returnedNotificationSetting)
        );

        insertedNotificationSetting = returnedNotificationSetting;
    }

    @Test
    void createNotificationSettingWithExistingId() throws Exception {
        // Create the NotificationSetting with an existing ID
        notificationSetting.setId("existing_id");
        NotificationSettingDTO notificationSettingDTO = notificationSettingMapper.toDto(notificationSetting);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationSettingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NotificationSetting in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkEmailEnabledIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationSetting.setEmailEnabled(null);

        // Create the NotificationSetting, which fails.
        NotificationSettingDTO notificationSettingDTO = notificationSettingMapper.toDto(notificationSetting);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationSettingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkSmsEnabledIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationSetting.setSmsEnabled(null);

        // Create the NotificationSetting, which fails.
        NotificationSettingDTO notificationSettingDTO = notificationSettingMapper.toDto(notificationSetting);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationSettingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkPushEnabledIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationSetting.setPushEnabled(null);

        // Create the NotificationSetting, which fails.
        NotificationSettingDTO notificationSettingDTO = notificationSettingMapper.toDto(notificationSetting);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationSettingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkInAppEnabledIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationSetting.setInAppEnabled(null);

        // Create the NotificationSetting, which fails.
        NotificationSettingDTO notificationSettingDTO = notificationSettingMapper.toDto(notificationSetting);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationSettingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllNotificationSettings() {
        // Initialize the database
        insertedNotificationSetting = notificationSettingRepository.save(notificationSetting).block();

        // Get all the notificationSettingList
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
            .value(hasItem(notificationSetting.getId()))
            .jsonPath("$.[*].emailEnabled")
            .value(hasItem(DEFAULT_EMAIL_ENABLED))
            .jsonPath("$.[*].smsEnabled")
            .value(hasItem(DEFAULT_SMS_ENABLED))
            .jsonPath("$.[*].pushEnabled")
            .value(hasItem(DEFAULT_PUSH_ENABLED))
            .jsonPath("$.[*].inAppEnabled")
            .value(hasItem(DEFAULT_IN_APP_ENABLED));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNotificationSettingsWithEagerRelationshipsIsEnabled() {
        when(notificationSettingServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(notificationSettingServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNotificationSettingsWithEagerRelationshipsIsNotEnabled() {
        when(notificationSettingServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(notificationSettingRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getNotificationSetting() {
        // Initialize the database
        insertedNotificationSetting = notificationSettingRepository.save(notificationSetting).block();

        // Get the notificationSetting
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, notificationSetting.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(notificationSetting.getId()))
            .jsonPath("$.emailEnabled")
            .value(is(DEFAULT_EMAIL_ENABLED))
            .jsonPath("$.smsEnabled")
            .value(is(DEFAULT_SMS_ENABLED))
            .jsonPath("$.pushEnabled")
            .value(is(DEFAULT_PUSH_ENABLED))
            .jsonPath("$.inAppEnabled")
            .value(is(DEFAULT_IN_APP_ENABLED));
    }

    @Test
    void getNonExistingNotificationSetting() {
        // Get the notificationSetting
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingNotificationSetting() throws Exception {
        // Initialize the database
        insertedNotificationSetting = notificationSettingRepository.save(notificationSetting).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationSetting
        NotificationSetting updatedNotificationSetting = notificationSettingRepository.findById(notificationSetting.getId()).block();
        updatedNotificationSetting
            .emailEnabled(UPDATED_EMAIL_ENABLED)
            .smsEnabled(UPDATED_SMS_ENABLED)
            .pushEnabled(UPDATED_PUSH_ENABLED)
            .inAppEnabled(UPDATED_IN_APP_ENABLED);
        NotificationSettingDTO notificationSettingDTO = notificationSettingMapper.toDto(updatedNotificationSetting);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, notificationSettingDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationSettingDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NotificationSetting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNotificationSettingToMatchAllProperties(updatedNotificationSetting);
    }

    @Test
    void putNonExistingNotificationSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationSetting.setId(UUID.randomUUID().toString());

        // Create the NotificationSetting
        NotificationSettingDTO notificationSettingDTO = notificationSettingMapper.toDto(notificationSetting);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, notificationSettingDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationSettingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NotificationSetting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchNotificationSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationSetting.setId(UUID.randomUUID().toString());

        // Create the NotificationSetting
        NotificationSettingDTO notificationSettingDTO = notificationSettingMapper.toDto(notificationSetting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationSettingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NotificationSetting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamNotificationSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationSetting.setId(UUID.randomUUID().toString());

        // Create the NotificationSetting
        NotificationSettingDTO notificationSettingDTO = notificationSettingMapper.toDto(notificationSetting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(notificationSettingDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the NotificationSetting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateNotificationSettingWithPatch() throws Exception {
        // Initialize the database
        insertedNotificationSetting = notificationSettingRepository.save(notificationSetting).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationSetting using partial update
        NotificationSetting partialUpdatedNotificationSetting = new NotificationSetting();
        partialUpdatedNotificationSetting.setId(notificationSetting.getId());

        partialUpdatedNotificationSetting
            .emailEnabled(UPDATED_EMAIL_ENABLED)
            .smsEnabled(UPDATED_SMS_ENABLED)
            .pushEnabled(UPDATED_PUSH_ENABLED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNotificationSetting.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedNotificationSetting))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NotificationSetting in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationSettingUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNotificationSetting, notificationSetting),
            getPersistedNotificationSetting(notificationSetting)
        );
    }

    @Test
    void fullUpdateNotificationSettingWithPatch() throws Exception {
        // Initialize the database
        insertedNotificationSetting = notificationSettingRepository.save(notificationSetting).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationSetting using partial update
        NotificationSetting partialUpdatedNotificationSetting = new NotificationSetting();
        partialUpdatedNotificationSetting.setId(notificationSetting.getId());

        partialUpdatedNotificationSetting
            .emailEnabled(UPDATED_EMAIL_ENABLED)
            .smsEnabled(UPDATED_SMS_ENABLED)
            .pushEnabled(UPDATED_PUSH_ENABLED)
            .inAppEnabled(UPDATED_IN_APP_ENABLED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNotificationSetting.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedNotificationSetting))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NotificationSetting in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationSettingUpdatableFieldsEquals(
            partialUpdatedNotificationSetting,
            getPersistedNotificationSetting(partialUpdatedNotificationSetting)
        );
    }

    @Test
    void patchNonExistingNotificationSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationSetting.setId(UUID.randomUUID().toString());

        // Create the NotificationSetting
        NotificationSettingDTO notificationSettingDTO = notificationSettingMapper.toDto(notificationSetting);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, notificationSettingDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(notificationSettingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NotificationSetting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchNotificationSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationSetting.setId(UUID.randomUUID().toString());

        // Create the NotificationSetting
        NotificationSettingDTO notificationSettingDTO = notificationSettingMapper.toDto(notificationSetting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(notificationSettingDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NotificationSetting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamNotificationSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationSetting.setId(UUID.randomUUID().toString());

        // Create the NotificationSetting
        NotificationSettingDTO notificationSettingDTO = notificationSettingMapper.toDto(notificationSetting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(notificationSettingDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the NotificationSetting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteNotificationSetting() {
        // Initialize the database
        insertedNotificationSetting = notificationSettingRepository.save(notificationSetting).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the notificationSetting
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, notificationSetting.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return notificationSettingRepository.count().block();
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

    protected NotificationSetting getPersistedNotificationSetting(NotificationSetting notificationSetting) {
        return notificationSettingRepository.findById(notificationSetting.getId()).block();
    }

    protected void assertPersistedNotificationSettingToMatchAllProperties(NotificationSetting expectedNotificationSetting) {
        assertNotificationSettingAllPropertiesEquals(
            expectedNotificationSetting,
            getPersistedNotificationSetting(expectedNotificationSetting)
        );
    }

    protected void assertPersistedNotificationSettingToMatchUpdatableProperties(NotificationSetting expectedNotificationSetting) {
        assertNotificationSettingAllUpdatablePropertiesEquals(
            expectedNotificationSetting,
            getPersistedNotificationSetting(expectedNotificationSetting)
        );
    }
}
