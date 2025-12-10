package com.stockapp.stockservice.web.rest;

import static com.stockapp.stockservice.domain.RecommendationAsserts.*;
import static com.stockapp.stockservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.stockservice.IntegrationTest;
import com.stockapp.stockservice.domain.Recommendation;
import com.stockapp.stockservice.repository.RecommendationRepository;
import com.stockapp.stockservice.service.RecommendationService;
import com.stockapp.stockservice.service.dto.RecommendationDTO;
import com.stockapp.stockservice.service.mapper.RecommendationMapper;
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
 * Integration tests for the {@link RecommendationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RecommendationResourceIT {

    private static final LocalDate DEFAULT_PERIOD = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_BUY = 0;
    private static final Integer UPDATED_BUY = 1;

    private static final Integer DEFAULT_HOLD = 0;
    private static final Integer UPDATED_HOLD = 1;

    private static final Integer DEFAULT_SELL = 0;
    private static final Integer UPDATED_SELL = 1;

    private static final Integer DEFAULT_STRONG_BUY = 0;
    private static final Integer UPDATED_STRONG_BUY = 1;

    private static final Integer DEFAULT_STRONG_SELL = 0;
    private static final Integer UPDATED_STRONG_SELL = 1;

    private static final String ENTITY_API_URL = "/api/recommendations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Mock
    private RecommendationRepository recommendationRepositoryMock;

    @Autowired
    private RecommendationMapper recommendationMapper;

    @Mock
    private RecommendationService recommendationServiceMock;

    @Autowired
    private WebTestClient webTestClient;

    private Recommendation recommendation;

    private Recommendation insertedRecommendation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recommendation createEntity() {
        return new Recommendation()
            .period(DEFAULT_PERIOD)
            .buy(DEFAULT_BUY)
            .hold(DEFAULT_HOLD)
            .sell(DEFAULT_SELL)
            .strongBuy(DEFAULT_STRONG_BUY)
            .strongSell(DEFAULT_STRONG_SELL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recommendation createUpdatedEntity() {
        return new Recommendation()
            .period(UPDATED_PERIOD)
            .buy(UPDATED_BUY)
            .hold(UPDATED_HOLD)
            .sell(UPDATED_SELL)
            .strongBuy(UPDATED_STRONG_BUY)
            .strongSell(UPDATED_STRONG_SELL);
    }

    @BeforeEach
    void initTest() {
        recommendation = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRecommendation != null) {
            recommendationRepository.delete(insertedRecommendation).block();
            insertedRecommendation = null;
        }
    }

    @Test
    void createRecommendation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);
        var returnedRecommendationDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(recommendationDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(RecommendationDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Recommendation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRecommendation = recommendationMapper.toEntity(returnedRecommendationDTO);
        assertRecommendationUpdatableFieldsEquals(returnedRecommendation, getPersistedRecommendation(returnedRecommendation));

        insertedRecommendation = returnedRecommendation;
    }

    @Test
    void createRecommendationWithExistingId() throws Exception {
        // Create the Recommendation with an existing ID
        recommendation.setId("existing_id");
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(recommendationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Recommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkPeriodIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recommendation.setPeriod(null);

        // Create the Recommendation, which fails.
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(recommendationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllRecommendations() {
        // Initialize the database
        insertedRecommendation = recommendationRepository.save(recommendation).block();

        // Get all the recommendationList
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
            .value(hasItem(recommendation.getId()))
            .jsonPath("$.[*].period")
            .value(hasItem(DEFAULT_PERIOD.toString()))
            .jsonPath("$.[*].buy")
            .value(hasItem(DEFAULT_BUY))
            .jsonPath("$.[*].hold")
            .value(hasItem(DEFAULT_HOLD))
            .jsonPath("$.[*].sell")
            .value(hasItem(DEFAULT_SELL))
            .jsonPath("$.[*].strongBuy")
            .value(hasItem(DEFAULT_STRONG_BUY))
            .jsonPath("$.[*].strongSell")
            .value(hasItem(DEFAULT_STRONG_SELL));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecommendationsWithEagerRelationshipsIsEnabled() {
        when(recommendationServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(recommendationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecommendationsWithEagerRelationshipsIsNotEnabled() {
        when(recommendationServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(recommendationRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getRecommendation() {
        // Initialize the database
        insertedRecommendation = recommendationRepository.save(recommendation).block();

        // Get the recommendation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, recommendation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(recommendation.getId()))
            .jsonPath("$.period")
            .value(is(DEFAULT_PERIOD.toString()))
            .jsonPath("$.buy")
            .value(is(DEFAULT_BUY))
            .jsonPath("$.hold")
            .value(is(DEFAULT_HOLD))
            .jsonPath("$.sell")
            .value(is(DEFAULT_SELL))
            .jsonPath("$.strongBuy")
            .value(is(DEFAULT_STRONG_BUY))
            .jsonPath("$.strongSell")
            .value(is(DEFAULT_STRONG_SELL));
    }

    @Test
    void getNonExistingRecommendation() {
        // Get the recommendation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingRecommendation() throws Exception {
        // Initialize the database
        insertedRecommendation = recommendationRepository.save(recommendation).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recommendation
        Recommendation updatedRecommendation = recommendationRepository.findById(recommendation.getId()).block();
        updatedRecommendation
            .period(UPDATED_PERIOD)
            .buy(UPDATED_BUY)
            .hold(UPDATED_HOLD)
            .sell(UPDATED_SELL)
            .strongBuy(UPDATED_STRONG_BUY)
            .strongSell(UPDATED_STRONG_SELL);
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(updatedRecommendation);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, recommendationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(recommendationDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Recommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecommendationToMatchAllProperties(updatedRecommendation);
    }

    @Test
    void putNonExistingRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recommendation.setId(UUID.randomUUID().toString());

        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, recommendationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(recommendationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Recommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recommendation.setId(UUID.randomUUID().toString());

        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(recommendationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Recommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recommendation.setId(UUID.randomUUID().toString());

        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(recommendationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Recommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRecommendationWithPatch() throws Exception {
        // Initialize the database
        insertedRecommendation = recommendationRepository.save(recommendation).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recommendation using partial update
        Recommendation partialUpdatedRecommendation = new Recommendation();
        partialUpdatedRecommendation.setId(recommendation.getId());

        partialUpdatedRecommendation.period(UPDATED_PERIOD).hold(UPDATED_HOLD).strongSell(UPDATED_STRONG_SELL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRecommendation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedRecommendation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Recommendation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecommendationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRecommendation, recommendation),
            getPersistedRecommendation(recommendation)
        );
    }

    @Test
    void fullUpdateRecommendationWithPatch() throws Exception {
        // Initialize the database
        insertedRecommendation = recommendationRepository.save(recommendation).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recommendation using partial update
        Recommendation partialUpdatedRecommendation = new Recommendation();
        partialUpdatedRecommendation.setId(recommendation.getId());

        partialUpdatedRecommendation
            .period(UPDATED_PERIOD)
            .buy(UPDATED_BUY)
            .hold(UPDATED_HOLD)
            .sell(UPDATED_SELL)
            .strongBuy(UPDATED_STRONG_BUY)
            .strongSell(UPDATED_STRONG_SELL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRecommendation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedRecommendation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Recommendation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecommendationUpdatableFieldsEquals(partialUpdatedRecommendation, getPersistedRecommendation(partialUpdatedRecommendation));
    }

    @Test
    void patchNonExistingRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recommendation.setId(UUID.randomUUID().toString());

        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, recommendationDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(recommendationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Recommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recommendation.setId(UUID.randomUUID().toString());

        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(recommendationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Recommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recommendation.setId(UUID.randomUUID().toString());

        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(recommendationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Recommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRecommendation() {
        // Initialize the database
        insertedRecommendation = recommendationRepository.save(recommendation).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recommendation
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, recommendation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recommendationRepository.count().block();
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

    protected Recommendation getPersistedRecommendation(Recommendation recommendation) {
        return recommendationRepository.findById(recommendation.getId()).block();
    }

    protected void assertPersistedRecommendationToMatchAllProperties(Recommendation expectedRecommendation) {
        assertRecommendationAllPropertiesEquals(expectedRecommendation, getPersistedRecommendation(expectedRecommendation));
    }

    protected void assertPersistedRecommendationToMatchUpdatableProperties(Recommendation expectedRecommendation) {
        assertRecommendationAllUpdatablePropertiesEquals(expectedRecommendation, getPersistedRecommendation(expectedRecommendation));
    }
}
