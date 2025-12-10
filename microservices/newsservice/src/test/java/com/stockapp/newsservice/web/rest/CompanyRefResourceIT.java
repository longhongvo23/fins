package com.stockapp.newsservice.web.rest;

import static com.stockapp.newsservice.domain.CompanyRefAsserts.*;
import static com.stockapp.newsservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.newsservice.IntegrationTest;
import com.stockapp.newsservice.domain.CompanyRef;
import com.stockapp.newsservice.repository.CompanyRefRepository;
import com.stockapp.newsservice.service.dto.CompanyRefDTO;
import com.stockapp.newsservice.service.mapper.CompanyRefMapper;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link CompanyRefResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CompanyRefResourceIT {

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/company-refs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompanyRefRepository companyRefRepository;

    @Autowired
    private CompanyRefMapper companyRefMapper;

    @Autowired
    private WebTestClient webTestClient;

    private CompanyRef companyRef;

    private CompanyRef insertedCompanyRef;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyRef createEntity() {
        return new CompanyRef().symbol(DEFAULT_SYMBOL).name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyRef createUpdatedEntity() {
        return new CompanyRef().symbol(UPDATED_SYMBOL).name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        companyRef = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCompanyRef != null) {
            companyRefRepository.delete(insertedCompanyRef).block();
            insertedCompanyRef = null;
        }
    }

    @Test
    void createCompanyRef() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CompanyRef
        CompanyRefDTO companyRefDTO = companyRefMapper.toDto(companyRef);
        var returnedCompanyRefDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyRefDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(CompanyRefDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the CompanyRef in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCompanyRef = companyRefMapper.toEntity(returnedCompanyRefDTO);
        assertCompanyRefUpdatableFieldsEquals(returnedCompanyRef, getPersistedCompanyRef(returnedCompanyRef));

        insertedCompanyRef = returnedCompanyRef;
    }

    @Test
    void createCompanyRefWithExistingId() throws Exception {
        // Create the CompanyRef with an existing ID
        companyRef.setId("existing_id");
        CompanyRefDTO companyRefDTO = companyRefMapper.toDto(companyRef);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyRefDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CompanyRef in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkSymbolIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        companyRef.setSymbol(null);

        // Create the CompanyRef, which fails.
        CompanyRefDTO companyRefDTO = companyRefMapper.toDto(companyRef);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyRefDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllCompanyRefs() {
        // Initialize the database
        insertedCompanyRef = companyRefRepository.save(companyRef).block();

        // Get all the companyRefList
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
            .value(hasItem(companyRef.getId()))
            .jsonPath("$.[*].symbol")
            .value(hasItem(DEFAULT_SYMBOL))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getCompanyRef() {
        // Initialize the database
        insertedCompanyRef = companyRefRepository.save(companyRef).block();

        // Get the companyRef
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, companyRef.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(companyRef.getId()))
            .jsonPath("$.symbol")
            .value(is(DEFAULT_SYMBOL))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingCompanyRef() {
        // Get the companyRef
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCompanyRef() throws Exception {
        // Initialize the database
        insertedCompanyRef = companyRefRepository.save(companyRef).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the companyRef
        CompanyRef updatedCompanyRef = companyRefRepository.findById(companyRef.getId()).block();
        updatedCompanyRef.symbol(UPDATED_SYMBOL).name(UPDATED_NAME);
        CompanyRefDTO companyRefDTO = companyRefMapper.toDto(updatedCompanyRef);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, companyRefDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyRefDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CompanyRef in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompanyRefToMatchAllProperties(updatedCompanyRef);
    }

    @Test
    void putNonExistingCompanyRef() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyRef.setId(UUID.randomUUID().toString());

        // Create the CompanyRef
        CompanyRefDTO companyRefDTO = companyRefMapper.toDto(companyRef);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, companyRefDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyRefDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CompanyRef in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCompanyRef() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyRef.setId(UUID.randomUUID().toString());

        // Create the CompanyRef
        CompanyRefDTO companyRefDTO = companyRefMapper.toDto(companyRef);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyRefDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CompanyRef in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCompanyRef() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyRef.setId(UUID.randomUUID().toString());

        // Create the CompanyRef
        CompanyRefDTO companyRefDTO = companyRefMapper.toDto(companyRef);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(companyRefDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CompanyRef in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCompanyRefWithPatch() throws Exception {
        // Initialize the database
        insertedCompanyRef = companyRefRepository.save(companyRef).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the companyRef using partial update
        CompanyRef partialUpdatedCompanyRef = new CompanyRef();
        partialUpdatedCompanyRef.setId(companyRef.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCompanyRef.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCompanyRef))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CompanyRef in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompanyRefUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCompanyRef, companyRef),
            getPersistedCompanyRef(companyRef)
        );
    }

    @Test
    void fullUpdateCompanyRefWithPatch() throws Exception {
        // Initialize the database
        insertedCompanyRef = companyRefRepository.save(companyRef).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the companyRef using partial update
        CompanyRef partialUpdatedCompanyRef = new CompanyRef();
        partialUpdatedCompanyRef.setId(companyRef.getId());

        partialUpdatedCompanyRef.symbol(UPDATED_SYMBOL).name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCompanyRef.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCompanyRef))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CompanyRef in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompanyRefUpdatableFieldsEquals(partialUpdatedCompanyRef, getPersistedCompanyRef(partialUpdatedCompanyRef));
    }

    @Test
    void patchNonExistingCompanyRef() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyRef.setId(UUID.randomUUID().toString());

        // Create the CompanyRef
        CompanyRefDTO companyRefDTO = companyRefMapper.toDto(companyRef);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, companyRefDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(companyRefDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CompanyRef in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCompanyRef() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyRef.setId(UUID.randomUUID().toString());

        // Create the CompanyRef
        CompanyRefDTO companyRefDTO = companyRefMapper.toDto(companyRef);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(companyRefDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CompanyRef in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCompanyRef() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyRef.setId(UUID.randomUUID().toString());

        // Create the CompanyRef
        CompanyRefDTO companyRefDTO = companyRefMapper.toDto(companyRef);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(companyRefDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CompanyRef in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCompanyRef() {
        // Initialize the database
        insertedCompanyRef = companyRefRepository.save(companyRef).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the companyRef
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, companyRef.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return companyRefRepository.count().block();
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

    protected CompanyRef getPersistedCompanyRef(CompanyRef companyRef) {
        return companyRefRepository.findById(companyRef.getId()).block();
    }

    protected void assertPersistedCompanyRefToMatchAllProperties(CompanyRef expectedCompanyRef) {
        assertCompanyRefAllPropertiesEquals(expectedCompanyRef, getPersistedCompanyRef(expectedCompanyRef));
    }

    protected void assertPersistedCompanyRefToMatchUpdatableProperties(CompanyRef expectedCompanyRef) {
        assertCompanyRefAllUpdatablePropertiesEquals(expectedCompanyRef, getPersistedCompanyRef(expectedCompanyRef));
    }
}
