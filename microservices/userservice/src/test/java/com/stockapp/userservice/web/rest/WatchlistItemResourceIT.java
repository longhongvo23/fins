package com.stockapp.userservice.web.rest;

import static com.stockapp.userservice.domain.WatchlistItemAsserts.*;
import static com.stockapp.userservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.userservice.IntegrationTest;
import com.stockapp.userservice.domain.WatchlistItem;
import com.stockapp.userservice.repository.WatchlistItemRepository;
import com.stockapp.userservice.service.WatchlistItemService;
import com.stockapp.userservice.service.dto.WatchlistItemDTO;
import com.stockapp.userservice.service.mapper.WatchlistItemMapper;
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
 * Integration tests for the {@link WatchlistItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class WatchlistItemResourceIT {

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final Instant DEFAULT_ADDED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ADDED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/watchlist-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WatchlistItemRepository watchlistItemRepository;

    @Mock
    private WatchlistItemRepository watchlistItemRepositoryMock;

    @Autowired
    private WatchlistItemMapper watchlistItemMapper;

    @Mock
    private WatchlistItemService watchlistItemServiceMock;

    @Autowired
    private WebTestClient webTestClient;

    private WatchlistItem watchlistItem;

    private WatchlistItem insertedWatchlistItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WatchlistItem createEntity() {
        return new WatchlistItem().symbol(DEFAULT_SYMBOL).addedAt(DEFAULT_ADDED_AT).notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WatchlistItem createUpdatedEntity() {
        return new WatchlistItem().symbol(UPDATED_SYMBOL).addedAt(UPDATED_ADDED_AT).notes(UPDATED_NOTES);
    }

    @BeforeEach
    void initTest() {
        watchlistItem = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedWatchlistItem != null) {
            watchlistItemRepository.delete(insertedWatchlistItem).block();
            insertedWatchlistItem = null;
        }
    }

    @Test
    void createWatchlistItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WatchlistItem
        WatchlistItemDTO watchlistItemDTO = watchlistItemMapper.toDto(watchlistItem);
        var returnedWatchlistItemDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(watchlistItemDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(WatchlistItemDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the WatchlistItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWatchlistItem = watchlistItemMapper.toEntity(returnedWatchlistItemDTO);
        assertWatchlistItemUpdatableFieldsEquals(returnedWatchlistItem, getPersistedWatchlistItem(returnedWatchlistItem));

        insertedWatchlistItem = returnedWatchlistItem;
    }

    @Test
    void createWatchlistItemWithExistingId() throws Exception {
        // Create the WatchlistItem with an existing ID
        watchlistItem.setId("existing_id");
        WatchlistItemDTO watchlistItemDTO = watchlistItemMapper.toDto(watchlistItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(watchlistItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WatchlistItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkSymbolIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        watchlistItem.setSymbol(null);

        // Create the WatchlistItem, which fails.
        WatchlistItemDTO watchlistItemDTO = watchlistItemMapper.toDto(watchlistItem);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(watchlistItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkAddedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        watchlistItem.setAddedAt(null);

        // Create the WatchlistItem, which fails.
        WatchlistItemDTO watchlistItemDTO = watchlistItemMapper.toDto(watchlistItem);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(watchlistItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllWatchlistItems() {
        // Initialize the database
        insertedWatchlistItem = watchlistItemRepository.save(watchlistItem).block();

        // Get all the watchlistItemList
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
            .value(hasItem(watchlistItem.getId()))
            .jsonPath("$.[*].symbol")
            .value(hasItem(DEFAULT_SYMBOL))
            .jsonPath("$.[*].addedAt")
            .value(hasItem(DEFAULT_ADDED_AT.toString()))
            .jsonPath("$.[*].notes")
            .value(hasItem(DEFAULT_NOTES));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWatchlistItemsWithEagerRelationshipsIsEnabled() {
        when(watchlistItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(watchlistItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWatchlistItemsWithEagerRelationshipsIsNotEnabled() {
        when(watchlistItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(watchlistItemRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getWatchlistItem() {
        // Initialize the database
        insertedWatchlistItem = watchlistItemRepository.save(watchlistItem).block();

        // Get the watchlistItem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, watchlistItem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(watchlistItem.getId()))
            .jsonPath("$.symbol")
            .value(is(DEFAULT_SYMBOL))
            .jsonPath("$.addedAt")
            .value(is(DEFAULT_ADDED_AT.toString()))
            .jsonPath("$.notes")
            .value(is(DEFAULT_NOTES));
    }

    @Test
    void getNonExistingWatchlistItem() {
        // Get the watchlistItem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingWatchlistItem() throws Exception {
        // Initialize the database
        insertedWatchlistItem = watchlistItemRepository.save(watchlistItem).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the watchlistItem
        WatchlistItem updatedWatchlistItem = watchlistItemRepository.findById(watchlistItem.getId()).block();
        updatedWatchlistItem.symbol(UPDATED_SYMBOL).addedAt(UPDATED_ADDED_AT).notes(UPDATED_NOTES);
        WatchlistItemDTO watchlistItemDTO = watchlistItemMapper.toDto(updatedWatchlistItem);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, watchlistItemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(watchlistItemDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the WatchlistItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWatchlistItemToMatchAllProperties(updatedWatchlistItem);
    }

    @Test
    void putNonExistingWatchlistItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        watchlistItem.setId(UUID.randomUUID().toString());

        // Create the WatchlistItem
        WatchlistItemDTO watchlistItemDTO = watchlistItemMapper.toDto(watchlistItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, watchlistItemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(watchlistItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WatchlistItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchWatchlistItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        watchlistItem.setId(UUID.randomUUID().toString());

        // Create the WatchlistItem
        WatchlistItemDTO watchlistItemDTO = watchlistItemMapper.toDto(watchlistItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(watchlistItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WatchlistItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamWatchlistItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        watchlistItem.setId(UUID.randomUUID().toString());

        // Create the WatchlistItem
        WatchlistItemDTO watchlistItemDTO = watchlistItemMapper.toDto(watchlistItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(watchlistItemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the WatchlistItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateWatchlistItemWithPatch() throws Exception {
        // Initialize the database
        insertedWatchlistItem = watchlistItemRepository.save(watchlistItem).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the watchlistItem using partial update
        WatchlistItem partialUpdatedWatchlistItem = new WatchlistItem();
        partialUpdatedWatchlistItem.setId(watchlistItem.getId());

        partialUpdatedWatchlistItem.addedAt(UPDATED_ADDED_AT).notes(UPDATED_NOTES);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWatchlistItem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedWatchlistItem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the WatchlistItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWatchlistItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWatchlistItem, watchlistItem),
            getPersistedWatchlistItem(watchlistItem)
        );
    }

    @Test
    void fullUpdateWatchlistItemWithPatch() throws Exception {
        // Initialize the database
        insertedWatchlistItem = watchlistItemRepository.save(watchlistItem).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the watchlistItem using partial update
        WatchlistItem partialUpdatedWatchlistItem = new WatchlistItem();
        partialUpdatedWatchlistItem.setId(watchlistItem.getId());

        partialUpdatedWatchlistItem.symbol(UPDATED_SYMBOL).addedAt(UPDATED_ADDED_AT).notes(UPDATED_NOTES);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWatchlistItem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedWatchlistItem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the WatchlistItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWatchlistItemUpdatableFieldsEquals(partialUpdatedWatchlistItem, getPersistedWatchlistItem(partialUpdatedWatchlistItem));
    }

    @Test
    void patchNonExistingWatchlistItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        watchlistItem.setId(UUID.randomUUID().toString());

        // Create the WatchlistItem
        WatchlistItemDTO watchlistItemDTO = watchlistItemMapper.toDto(watchlistItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, watchlistItemDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(watchlistItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WatchlistItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchWatchlistItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        watchlistItem.setId(UUID.randomUUID().toString());

        // Create the WatchlistItem
        WatchlistItemDTO watchlistItemDTO = watchlistItemMapper.toDto(watchlistItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(watchlistItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WatchlistItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamWatchlistItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        watchlistItem.setId(UUID.randomUUID().toString());

        // Create the WatchlistItem
        WatchlistItemDTO watchlistItemDTO = watchlistItemMapper.toDto(watchlistItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(watchlistItemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the WatchlistItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteWatchlistItem() {
        // Initialize the database
        insertedWatchlistItem = watchlistItemRepository.save(watchlistItem).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the watchlistItem
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, watchlistItem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return watchlistItemRepository.count().block();
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

    protected WatchlistItem getPersistedWatchlistItem(WatchlistItem watchlistItem) {
        return watchlistItemRepository.findById(watchlistItem.getId()).block();
    }

    protected void assertPersistedWatchlistItemToMatchAllProperties(WatchlistItem expectedWatchlistItem) {
        assertWatchlistItemAllPropertiesEquals(expectedWatchlistItem, getPersistedWatchlistItem(expectedWatchlistItem));
    }

    protected void assertPersistedWatchlistItemToMatchUpdatableProperties(WatchlistItem expectedWatchlistItem) {
        assertWatchlistItemAllUpdatablePropertiesEquals(expectedWatchlistItem, getPersistedWatchlistItem(expectedWatchlistItem));
    }
}
