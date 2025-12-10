package com.stockapp.newsservice.web.rest;

import static com.stockapp.newsservice.domain.CompanyNewsAsserts.*;
import static com.stockapp.newsservice.web.rest.TestUtil.createUpdateProxyForBean;
import static com.stockapp.newsservice.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.newsservice.IntegrationTest;
import com.stockapp.newsservice.domain.CompanyNews;
import com.stockapp.newsservice.repository.CompanyNewsRepository;
import com.stockapp.newsservice.service.CompanyNewsService;
import com.stockapp.newsservice.service.dto.CompanyNewsDTO;
import com.stockapp.newsservice.service.mapper.CompanyNewsMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link CompanyNewsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CompanyNewsResourceIT {

    private static final String DEFAULT_UUID = "AAAAAAAAAA";
    private static final String UPDATED_UUID = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SNIPPET = "AAAAAAAAAA";
    private static final String UPDATED_SNIPPET = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final Instant DEFAULT_PUBLISHED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PUBLISHED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_KEYWORDS = "AAAAAAAAAA";
    private static final String UPDATED_KEYWORDS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_RELEVANCE_SCORE = new BigDecimal(1);
    private static final BigDecimal UPDATED_RELEVANCE_SCORE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/company-news";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompanyNewsRepository companyNewsRepository;

    @Mock
    private CompanyNewsRepository companyNewsRepositoryMock;

    @Autowired
    private CompanyNewsMapper companyNewsMapper;

    @Mock
    private CompanyNewsService companyNewsServiceMock;

    @Autowired
    private WebTestClient webTestClient;

    private CompanyNews companyNews;

    private CompanyNews insertedCompanyNews;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyNews createEntity() {
        return new CompanyNews()
            .uuid(DEFAULT_UUID)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .snippet(DEFAULT_SNIPPET)
            .url(DEFAULT_URL)
            .imageUrl(DEFAULT_IMAGE_URL)
            .language(DEFAULT_LANGUAGE)
            .publishedAt(DEFAULT_PUBLISHED_AT)
            .source(DEFAULT_SOURCE)
            .keywords(DEFAULT_KEYWORDS)
            .relevanceScore(DEFAULT_RELEVANCE_SCORE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyNews createUpdatedEntity() {
        return new CompanyNews()
            .uuid(UPDATED_UUID)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .snippet(UPDATED_SNIPPET)
            .url(UPDATED_URL)
            .imageUrl(UPDATED_IMAGE_URL)
            .language(UPDATED_LANGUAGE)
            .publishedAt(UPDATED_PUBLISHED_AT)
            .source(UPDATED_SOURCE)
            .keywords(UPDATED_KEYWORDS)
            .relevanceScore(UPDATED_RELEVANCE_SCORE);
    }

    @BeforeEach
    void initTest() {
        companyNews = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCompanyNews != null) {
            companyNewsRepository.delete(insertedCompanyNews).block();
            insertedCompanyNews = null;
        }
    }

    @Test
    void createCompanyNews() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CompanyNews
        CompanyNewsDTO companyNewsDTO = companyNewsMapper.toDto(companyNews);
        var returnedCompanyNewsDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyNewsDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(CompanyNewsDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the CompanyNews in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCompanyNews = companyNewsMapper.toEntity(returnedCompanyNewsDTO);
        assertCompanyNewsUpdatableFieldsEquals(returnedCompanyNews, getPersistedCompanyNews(returnedCompanyNews));

        insertedCompanyNews = returnedCompanyNews;
    }

    @Test
    void createCompanyNewsWithExistingId() throws Exception {
        // Create the CompanyNews with an existing ID
        companyNews.setId("existing_id");
        CompanyNewsDTO companyNewsDTO = companyNewsMapper.toDto(companyNews);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyNewsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CompanyNews in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        companyNews.setUuid(null);

        // Create the CompanyNews, which fails.
        CompanyNewsDTO companyNewsDTO = companyNewsMapper.toDto(companyNews);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyNewsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        companyNews.setTitle(null);

        // Create the CompanyNews, which fails.
        CompanyNewsDTO companyNewsDTO = companyNewsMapper.toDto(companyNews);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyNewsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        companyNews.setUrl(null);

        // Create the CompanyNews, which fails.
        CompanyNewsDTO companyNewsDTO = companyNewsMapper.toDto(companyNews);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyNewsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkPublishedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        companyNews.setPublishedAt(null);

        // Create the CompanyNews, which fails.
        CompanyNewsDTO companyNewsDTO = companyNewsMapper.toDto(companyNews);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyNewsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkSourceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        companyNews.setSource(null);

        // Create the CompanyNews, which fails.
        CompanyNewsDTO companyNewsDTO = companyNewsMapper.toDto(companyNews);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyNewsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllCompanyNews() {
        // Initialize the database
        insertedCompanyNews = companyNewsRepository.save(companyNews).block();

        // Get all the companyNewsList
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
            .value(hasItem(companyNews.getId()))
            .jsonPath("$.[*].uuid")
            .value(hasItem(DEFAULT_UUID))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].snippet")
            .value(hasItem(DEFAULT_SNIPPET))
            .jsonPath("$.[*].url")
            .value(hasItem(DEFAULT_URL))
            .jsonPath("$.[*].imageUrl")
            .value(hasItem(DEFAULT_IMAGE_URL))
            .jsonPath("$.[*].language")
            .value(hasItem(DEFAULT_LANGUAGE))
            .jsonPath("$.[*].publishedAt")
            .value(hasItem(DEFAULT_PUBLISHED_AT.toString()))
            .jsonPath("$.[*].source")
            .value(hasItem(DEFAULT_SOURCE))
            .jsonPath("$.[*].keywords")
            .value(hasItem(DEFAULT_KEYWORDS))
            .jsonPath("$.[*].relevanceScore")
            .value(hasItem(sameNumber(DEFAULT_RELEVANCE_SCORE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCompanyNewsWithEagerRelationshipsIsEnabled() {
        when(companyNewsServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(companyNewsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCompanyNewsWithEagerRelationshipsIsNotEnabled() {
        when(companyNewsServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(companyNewsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getCompanyNews() {
        // Initialize the database
        insertedCompanyNews = companyNewsRepository.save(companyNews).block();

        // Get the companyNews
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, companyNews.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(companyNews.getId()))
            .jsonPath("$.uuid")
            .value(is(DEFAULT_UUID))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.snippet")
            .value(is(DEFAULT_SNIPPET))
            .jsonPath("$.url")
            .value(is(DEFAULT_URL))
            .jsonPath("$.imageUrl")
            .value(is(DEFAULT_IMAGE_URL))
            .jsonPath("$.language")
            .value(is(DEFAULT_LANGUAGE))
            .jsonPath("$.publishedAt")
            .value(is(DEFAULT_PUBLISHED_AT.toString()))
            .jsonPath("$.source")
            .value(is(DEFAULT_SOURCE))
            .jsonPath("$.keywords")
            .value(is(DEFAULT_KEYWORDS))
            .jsonPath("$.relevanceScore")
            .value(is(sameNumber(DEFAULT_RELEVANCE_SCORE)));
    }

    @Test
    void getNonExistingCompanyNews() {
        // Get the companyNews
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCompanyNews() throws Exception {
        // Initialize the database
        insertedCompanyNews = companyNewsRepository.save(companyNews).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the companyNews
        CompanyNews updatedCompanyNews = companyNewsRepository.findById(companyNews.getId()).block();
        updatedCompanyNews
            .uuid(UPDATED_UUID)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .snippet(UPDATED_SNIPPET)
            .url(UPDATED_URL)
            .imageUrl(UPDATED_IMAGE_URL)
            .language(UPDATED_LANGUAGE)
            .publishedAt(UPDATED_PUBLISHED_AT)
            .source(UPDATED_SOURCE)
            .keywords(UPDATED_KEYWORDS)
            .relevanceScore(UPDATED_RELEVANCE_SCORE);
        CompanyNewsDTO companyNewsDTO = companyNewsMapper.toDto(updatedCompanyNews);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, companyNewsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyNewsDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CompanyNews in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompanyNewsToMatchAllProperties(updatedCompanyNews);
    }

    @Test
    void putNonExistingCompanyNews() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyNews.setId(UUID.randomUUID().toString());

        // Create the CompanyNews
        CompanyNewsDTO companyNewsDTO = companyNewsMapper.toDto(companyNews);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, companyNewsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyNewsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CompanyNews in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCompanyNews() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyNews.setId(UUID.randomUUID().toString());

        // Create the CompanyNews
        CompanyNewsDTO companyNewsDTO = companyNewsMapper.toDto(companyNews);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyNewsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CompanyNews in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCompanyNews() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyNews.setId(UUID.randomUUID().toString());

        // Create the CompanyNews
        CompanyNewsDTO companyNewsDTO = companyNewsMapper.toDto(companyNews);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyNewsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CompanyNews in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCompanyNewsWithPatch() throws Exception {
        // Initialize the database
        insertedCompanyNews = companyNewsRepository.save(companyNews).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the companyNews using partial update
        CompanyNews partialUpdatedCompanyNews = new CompanyNews();
        partialUpdatedCompanyNews.setId(companyNews.getId());

        partialUpdatedCompanyNews
            .snippet(UPDATED_SNIPPET)
            .url(UPDATED_URL)
            .imageUrl(UPDATED_IMAGE_URL)
            .language(UPDATED_LANGUAGE)
            .publishedAt(UPDATED_PUBLISHED_AT)
            .keywords(UPDATED_KEYWORDS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCompanyNews.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCompanyNews))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CompanyNews in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompanyNewsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCompanyNews, companyNews),
            getPersistedCompanyNews(companyNews)
        );
    }

    @Test
    void fullUpdateCompanyNewsWithPatch() throws Exception {
        // Initialize the database
        insertedCompanyNews = companyNewsRepository.save(companyNews).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the companyNews using partial update
        CompanyNews partialUpdatedCompanyNews = new CompanyNews();
        partialUpdatedCompanyNews.setId(companyNews.getId());

        partialUpdatedCompanyNews
            .uuid(UPDATED_UUID)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .snippet(UPDATED_SNIPPET)
            .url(UPDATED_URL)
            .imageUrl(UPDATED_IMAGE_URL)
            .language(UPDATED_LANGUAGE)
            .publishedAt(UPDATED_PUBLISHED_AT)
            .source(UPDATED_SOURCE)
            .keywords(UPDATED_KEYWORDS)
            .relevanceScore(UPDATED_RELEVANCE_SCORE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCompanyNews.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCompanyNews))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CompanyNews in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompanyNewsUpdatableFieldsEquals(partialUpdatedCompanyNews, getPersistedCompanyNews(partialUpdatedCompanyNews));
    }

    @Test
    void patchNonExistingCompanyNews() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyNews.setId(UUID.randomUUID().toString());

        // Create the CompanyNews
        CompanyNewsDTO companyNewsDTO = companyNewsMapper.toDto(companyNews);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, companyNewsDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(companyNewsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CompanyNews in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCompanyNews() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyNews.setId(UUID.randomUUID().toString());

        // Create the CompanyNews
        CompanyNewsDTO companyNewsDTO = companyNewsMapper.toDto(companyNews);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(companyNewsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CompanyNews in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCompanyNews() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyNews.setId(UUID.randomUUID().toString());

        // Create the CompanyNews
        CompanyNewsDTO companyNewsDTO = companyNewsMapper.toDto(companyNews);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(companyNewsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CompanyNews in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCompanyNews() {
        // Initialize the database
        insertedCompanyNews = companyNewsRepository.save(companyNews).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the companyNews
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, companyNews.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return companyNewsRepository.count().block();
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

    protected CompanyNews getPersistedCompanyNews(CompanyNews companyNews) {
        return companyNewsRepository.findById(companyNews.getId()).block();
    }

    protected void assertPersistedCompanyNewsToMatchAllProperties(CompanyNews expectedCompanyNews) {
        assertCompanyNewsAllPropertiesEquals(expectedCompanyNews, getPersistedCompanyNews(expectedCompanyNews));
    }

    protected void assertPersistedCompanyNewsToMatchUpdatableProperties(CompanyNews expectedCompanyNews) {
        assertCompanyNewsAllUpdatablePropertiesEquals(expectedCompanyNews, getPersistedCompanyNews(expectedCompanyNews));
    }
}
