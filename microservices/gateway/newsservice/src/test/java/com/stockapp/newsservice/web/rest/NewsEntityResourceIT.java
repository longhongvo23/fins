package com.stockapp.newsservice.web.rest;

import static com.stockapp.newsservice.domain.NewsEntityAsserts.*;
import static com.stockapp.newsservice.web.rest.TestUtil.createUpdateProxyForBean;
import static com.stockapp.newsservice.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.newsservice.IntegrationTest;
import com.stockapp.newsservice.domain.NewsEntity;
import com.stockapp.newsservice.repository.NewsEntityRepository;
import com.stockapp.newsservice.service.NewsEntityService;
import com.stockapp.newsservice.service.dto.NewsEntityDTO;
import com.stockapp.newsservice.service.mapper.NewsEntityMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link NewsEntityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class NewsEntityResourceIT {

    private static final String DEFAULT_NEWS_UUID = "AAAAAAAAAA";
    private static final String UPDATED_NEWS_UUID = "BBBBBBBBBB";

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EXCHANGE = "AAAAAAAAAA";
    private static final String UPDATED_EXCHANGE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_INDUSTRY = "AAAAAAAAAA";
    private static final String UPDATED_INDUSTRY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MATCH_SCORE = new BigDecimal(1);
    private static final BigDecimal UPDATED_MATCH_SCORE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SENTIMENT_SCORE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SENTIMENT_SCORE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/news-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NewsEntityRepository newsEntityRepository;

    @Mock
    private NewsEntityRepository newsEntityRepositoryMock;

    @Autowired
    private NewsEntityMapper newsEntityMapper;

    @Mock
    private NewsEntityService newsEntityServiceMock;

    @Autowired
    private WebTestClient webTestClient;

    private NewsEntity newsEntity;

    private NewsEntity insertedNewsEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NewsEntity createEntity() {
        return new NewsEntity()
            .newsUuid(DEFAULT_NEWS_UUID)
            .symbol(DEFAULT_SYMBOL)
            .name(DEFAULT_NAME)
            .exchange(DEFAULT_EXCHANGE)
            .country(DEFAULT_COUNTRY)
            .type(DEFAULT_TYPE)
            .industry(DEFAULT_INDUSTRY)
            .matchScore(DEFAULT_MATCH_SCORE)
            .sentimentScore(DEFAULT_SENTIMENT_SCORE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NewsEntity createUpdatedEntity() {
        return new NewsEntity()
            .newsUuid(UPDATED_NEWS_UUID)
            .symbol(UPDATED_SYMBOL)
            .name(UPDATED_NAME)
            .exchange(UPDATED_EXCHANGE)
            .country(UPDATED_COUNTRY)
            .type(UPDATED_TYPE)
            .industry(UPDATED_INDUSTRY)
            .matchScore(UPDATED_MATCH_SCORE)
            .sentimentScore(UPDATED_SENTIMENT_SCORE);
    }

    @BeforeEach
    void initTest() {
        newsEntity = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedNewsEntity != null) {
            newsEntityRepository.delete(insertedNewsEntity).block();
            insertedNewsEntity = null;
        }
    }

    @Test
    void createNewsEntity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NewsEntity
        NewsEntityDTO newsEntityDTO = newsEntityMapper.toDto(newsEntity);
        var returnedNewsEntityDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(newsEntityDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(NewsEntityDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the NewsEntity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNewsEntity = newsEntityMapper.toEntity(returnedNewsEntityDTO);
        assertNewsEntityUpdatableFieldsEquals(returnedNewsEntity, getPersistedNewsEntity(returnedNewsEntity));

        insertedNewsEntity = returnedNewsEntity;
    }

    @Test
    void createNewsEntityWithExistingId() throws Exception {
        // Create the NewsEntity with an existing ID
        newsEntity.setId("existing_id");
        NewsEntityDTO newsEntityDTO = newsEntityMapper.toDto(newsEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(newsEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NewsEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNewsUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        newsEntity.setNewsUuid(null);

        // Create the NewsEntity, which fails.
        NewsEntityDTO newsEntityDTO = newsEntityMapper.toDto(newsEntity);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(newsEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkSymbolIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        newsEntity.setSymbol(null);

        // Create the NewsEntity, which fails.
        NewsEntityDTO newsEntityDTO = newsEntityMapper.toDto(newsEntity);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(newsEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllNewsEntities() {
        // Initialize the database
        insertedNewsEntity = newsEntityRepository.save(newsEntity).block();

        // Get all the newsEntityList
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
            .value(hasItem(newsEntity.getId()))
            .jsonPath("$.[*].newsUuid")
            .value(hasItem(DEFAULT_NEWS_UUID))
            .jsonPath("$.[*].symbol")
            .value(hasItem(DEFAULT_SYMBOL))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].exchange")
            .value(hasItem(DEFAULT_EXCHANGE))
            .jsonPath("$.[*].country")
            .value(hasItem(DEFAULT_COUNTRY))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].industry")
            .value(hasItem(DEFAULT_INDUSTRY))
            .jsonPath("$.[*].matchScore")
            .value(hasItem(sameNumber(DEFAULT_MATCH_SCORE)))
            .jsonPath("$.[*].sentimentScore")
            .value(hasItem(sameNumber(DEFAULT_SENTIMENT_SCORE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNewsEntitiesWithEagerRelationshipsIsEnabled() {
        when(newsEntityServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(newsEntityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNewsEntitiesWithEagerRelationshipsIsNotEnabled() {
        when(newsEntityServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(newsEntityRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getNewsEntity() {
        // Initialize the database
        insertedNewsEntity = newsEntityRepository.save(newsEntity).block();

        // Get the newsEntity
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, newsEntity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(newsEntity.getId()))
            .jsonPath("$.newsUuid")
            .value(is(DEFAULT_NEWS_UUID))
            .jsonPath("$.symbol")
            .value(is(DEFAULT_SYMBOL))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.exchange")
            .value(is(DEFAULT_EXCHANGE))
            .jsonPath("$.country")
            .value(is(DEFAULT_COUNTRY))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE))
            .jsonPath("$.industry")
            .value(is(DEFAULT_INDUSTRY))
            .jsonPath("$.matchScore")
            .value(is(sameNumber(DEFAULT_MATCH_SCORE)))
            .jsonPath("$.sentimentScore")
            .value(is(sameNumber(DEFAULT_SENTIMENT_SCORE)));
    }

    @Test
    void getNonExistingNewsEntity() {
        // Get the newsEntity
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingNewsEntity() throws Exception {
        // Initialize the database
        insertedNewsEntity = newsEntityRepository.save(newsEntity).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the newsEntity
        NewsEntity updatedNewsEntity = newsEntityRepository.findById(newsEntity.getId()).block();
        updatedNewsEntity
            .newsUuid(UPDATED_NEWS_UUID)
            .symbol(UPDATED_SYMBOL)
            .name(UPDATED_NAME)
            .exchange(UPDATED_EXCHANGE)
            .country(UPDATED_COUNTRY)
            .type(UPDATED_TYPE)
            .industry(UPDATED_INDUSTRY)
            .matchScore(UPDATED_MATCH_SCORE)
            .sentimentScore(UPDATED_SENTIMENT_SCORE);
        NewsEntityDTO newsEntityDTO = newsEntityMapper.toDto(updatedNewsEntity);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, newsEntityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(newsEntityDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NewsEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNewsEntityToMatchAllProperties(updatedNewsEntity);
    }

    @Test
    void putNonExistingNewsEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        newsEntity.setId(UUID.randomUUID().toString());

        // Create the NewsEntity
        NewsEntityDTO newsEntityDTO = newsEntityMapper.toDto(newsEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, newsEntityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(newsEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NewsEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchNewsEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        newsEntity.setId(UUID.randomUUID().toString());

        // Create the NewsEntity
        NewsEntityDTO newsEntityDTO = newsEntityMapper.toDto(newsEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(newsEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NewsEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamNewsEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        newsEntity.setId(UUID.randomUUID().toString());

        // Create the NewsEntity
        NewsEntityDTO newsEntityDTO = newsEntityMapper.toDto(newsEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(newsEntityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the NewsEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateNewsEntityWithPatch() throws Exception {
        // Initialize the database
        insertedNewsEntity = newsEntityRepository.save(newsEntity).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the newsEntity using partial update
        NewsEntity partialUpdatedNewsEntity = new NewsEntity();
        partialUpdatedNewsEntity.setId(newsEntity.getId());

        partialUpdatedNewsEntity
            .name(UPDATED_NAME)
            .exchange(UPDATED_EXCHANGE)
            .country(UPDATED_COUNTRY)
            .type(UPDATED_TYPE)
            .industry(UPDATED_INDUSTRY)
            .matchScore(UPDATED_MATCH_SCORE)
            .sentimentScore(UPDATED_SENTIMENT_SCORE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNewsEntity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedNewsEntity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NewsEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNewsEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNewsEntity, newsEntity),
            getPersistedNewsEntity(newsEntity)
        );
    }

    @Test
    void fullUpdateNewsEntityWithPatch() throws Exception {
        // Initialize the database
        insertedNewsEntity = newsEntityRepository.save(newsEntity).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the newsEntity using partial update
        NewsEntity partialUpdatedNewsEntity = new NewsEntity();
        partialUpdatedNewsEntity.setId(newsEntity.getId());

        partialUpdatedNewsEntity
            .newsUuid(UPDATED_NEWS_UUID)
            .symbol(UPDATED_SYMBOL)
            .name(UPDATED_NAME)
            .exchange(UPDATED_EXCHANGE)
            .country(UPDATED_COUNTRY)
            .type(UPDATED_TYPE)
            .industry(UPDATED_INDUSTRY)
            .matchScore(UPDATED_MATCH_SCORE)
            .sentimentScore(UPDATED_SENTIMENT_SCORE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNewsEntity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedNewsEntity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NewsEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNewsEntityUpdatableFieldsEquals(partialUpdatedNewsEntity, getPersistedNewsEntity(partialUpdatedNewsEntity));
    }

    @Test
    void patchNonExistingNewsEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        newsEntity.setId(UUID.randomUUID().toString());

        // Create the NewsEntity
        NewsEntityDTO newsEntityDTO = newsEntityMapper.toDto(newsEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, newsEntityDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(newsEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NewsEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchNewsEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        newsEntity.setId(UUID.randomUUID().toString());

        // Create the NewsEntity
        NewsEntityDTO newsEntityDTO = newsEntityMapper.toDto(newsEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(newsEntityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NewsEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamNewsEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        newsEntity.setId(UUID.randomUUID().toString());

        // Create the NewsEntity
        NewsEntityDTO newsEntityDTO = newsEntityMapper.toDto(newsEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(newsEntityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the NewsEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteNewsEntity() {
        // Initialize the database
        insertedNewsEntity = newsEntityRepository.save(newsEntity).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the newsEntity
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, newsEntity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return newsEntityRepository.count().block();
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

    protected NewsEntity getPersistedNewsEntity(NewsEntity newsEntity) {
        return newsEntityRepository.findById(newsEntity.getId()).block();
    }

    protected void assertPersistedNewsEntityToMatchAllProperties(NewsEntity expectedNewsEntity) {
        assertNewsEntityAllPropertiesEquals(expectedNewsEntity, getPersistedNewsEntity(expectedNewsEntity));
    }

    protected void assertPersistedNewsEntityToMatchUpdatableProperties(NewsEntity expectedNewsEntity) {
        assertNewsEntityAllUpdatablePropertiesEquals(expectedNewsEntity, getPersistedNewsEntity(expectedNewsEntity));
    }
}
