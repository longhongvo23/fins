package com.stockapp.userservice.web.rest;

import static com.stockapp.userservice.domain.AuthorityAsserts.*;
import static com.stockapp.userservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.userservice.IntegrationTest;
import com.stockapp.userservice.domain.Authority;
import com.stockapp.userservice.repository.AuthorityRepository;
import com.stockapp.userservice.service.dto.AuthorityDTO;
import com.stockapp.userservice.service.mapper.AuthorityMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link AuthorityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AuthorityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SYSTEM = false;
    private static final Boolean UPDATED_IS_SYSTEM = true;

    private static final String ENTITY_API_URL = "/api/authorities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private AuthorityMapper authorityMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Authority authority;

    private Authority insertedAuthority;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authority createEntity() {
        return new Authority()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .isSystem(DEFAULT_IS_SYSTEM);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authority createUpdatedEntity() {
        return new Authority()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .isSystem(UPDATED_IS_SYSTEM);
    }

    @BeforeEach
    void initTest() {
        authority = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAuthority != null) {
            authorityRepository.delete(insertedAuthority).block();
            insertedAuthority = null;
        }
    }

    @Test
    void createAuthority() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Authority
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);
        var returnedAuthorityDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(authorityDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(AuthorityDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Authority in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAuthority = authorityMapper.toEntity(returnedAuthorityDTO);
        assertAuthorityUpdatableFieldsEquals(returnedAuthority, getPersistedAuthority(returnedAuthority));

        insertedAuthority = returnedAuthority;
    }

    @Test
    void createAuthorityWithExistingId() throws Exception {
        // Create the Authority with an existing ID
        authority.setId("existing_id");
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(authorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Authority in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        authority.setName(null);

        // Create the Authority, which fails.
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(authorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkIsSystemIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        authority.setIsSystem(null);

        // Create the Authority, which fails.
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(authorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllAuthorities() {
        // Initialize the database
        insertedAuthority = authorityRepository.save(authority).block();

        // Get all the authorityList
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
            .value(hasItem(authority.getId()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].isSystem")
            .value(hasItem(DEFAULT_IS_SYSTEM));
    }

    @Test
    void getAuthority() {
        // Initialize the database
        insertedAuthority = authorityRepository.save(authority).block();

        // Get the authority
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, authority.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(authority.getId()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.createdDate")
            .value(is(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.isSystem")
            .value(is(DEFAULT_IS_SYSTEM));
    }

    @Test
    void getNonExistingAuthority() {
        // Get the authority
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAuthority() throws Exception {
        // Initialize the database
        insertedAuthority = authorityRepository.save(authority).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the authority
        Authority updatedAuthority = authorityRepository.findById(authority.getId()).block();
        updatedAuthority
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .isSystem(UPDATED_IS_SYSTEM);
        AuthorityDTO authorityDTO = authorityMapper.toDto(updatedAuthority);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, authorityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(authorityDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Authority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAuthorityToMatchAllProperties(updatedAuthority);
    }

    @Test
    void putNonExistingAuthority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authority.setId(UUID.randomUUID().toString());

        // Create the Authority
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, authorityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(authorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Authority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAuthority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authority.setId(UUID.randomUUID().toString());

        // Create the Authority
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(authorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Authority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAuthority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authority.setId(UUID.randomUUID().toString());

        // Create the Authority
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(authorityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Authority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAuthorityWithPatch() throws Exception {
        // Initialize the database
        insertedAuthority = authorityRepository.save(authority).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the authority using partial update
        Authority partialUpdatedAuthority = new Authority();
        partialUpdatedAuthority.setId(authority.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAuthority.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAuthority))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Authority in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAuthorityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAuthority, authority),
            getPersistedAuthority(authority)
        );
    }

    @Test
    void fullUpdateAuthorityWithPatch() throws Exception {
        // Initialize the database
        insertedAuthority = authorityRepository.save(authority).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the authority using partial update
        Authority partialUpdatedAuthority = new Authority();
        partialUpdatedAuthority.setId(authority.getId());

        partialUpdatedAuthority
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .isSystem(UPDATED_IS_SYSTEM);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAuthority.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAuthority))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Authority in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAuthorityUpdatableFieldsEquals(partialUpdatedAuthority, getPersistedAuthority(partialUpdatedAuthority));
    }

    @Test
    void patchNonExistingAuthority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authority.setId(UUID.randomUUID().toString());

        // Create the Authority
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, authorityDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(authorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Authority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAuthority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authority.setId(UUID.randomUUID().toString());

        // Create the Authority
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(authorityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Authority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAuthority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authority.setId(UUID.randomUUID().toString());

        // Create the Authority
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(authorityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Authority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAuthority() {
        // Initialize the database
        insertedAuthority = authorityRepository.save(authority).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the authority
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, authority.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return authorityRepository.count().block();
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

    protected Authority getPersistedAuthority(Authority authority) {
        return authorityRepository.findById(authority.getId()).block();
    }

    protected void assertPersistedAuthorityToMatchAllProperties(Authority expectedAuthority) {
        assertAuthorityAllPropertiesEquals(expectedAuthority, getPersistedAuthority(expectedAuthority));
    }

    protected void assertPersistedAuthorityToMatchUpdatableProperties(Authority expectedAuthority) {
        assertAuthorityAllUpdatablePropertiesEquals(expectedAuthority, getPersistedAuthority(expectedAuthority));
    }
}
