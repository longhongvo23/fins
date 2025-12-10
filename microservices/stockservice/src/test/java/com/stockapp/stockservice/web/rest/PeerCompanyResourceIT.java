package com.stockapp.stockservice.web.rest;

import static com.stockapp.stockservice.domain.PeerCompanyAsserts.*;
import static com.stockapp.stockservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockapp.stockservice.IntegrationTest;
import com.stockapp.stockservice.domain.PeerCompany;
import com.stockapp.stockservice.repository.PeerCompanyRepository;
import com.stockapp.stockservice.service.PeerCompanyService;
import com.stockapp.stockservice.service.dto.PeerCompanyDTO;
import com.stockapp.stockservice.service.mapper.PeerCompanyMapper;
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
 * Integration tests for the {@link PeerCompanyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PeerCompanyResourceIT {

    private static final String DEFAULT_PEER_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_PEER_SYMBOL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/peer-companies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PeerCompanyRepository peerCompanyRepository;

    @Mock
    private PeerCompanyRepository peerCompanyRepositoryMock;

    @Autowired
    private PeerCompanyMapper peerCompanyMapper;

    @Mock
    private PeerCompanyService peerCompanyServiceMock;

    @Autowired
    private WebTestClient webTestClient;

    private PeerCompany peerCompany;

    private PeerCompany insertedPeerCompany;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeerCompany createEntity() {
        return new PeerCompany().peerSymbol(DEFAULT_PEER_SYMBOL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeerCompany createUpdatedEntity() {
        return new PeerCompany().peerSymbol(UPDATED_PEER_SYMBOL);
    }

    @BeforeEach
    void initTest() {
        peerCompany = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPeerCompany != null) {
            peerCompanyRepository.delete(insertedPeerCompany).block();
            insertedPeerCompany = null;
        }
    }

    @Test
    void createPeerCompany() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PeerCompany
        PeerCompanyDTO peerCompanyDTO = peerCompanyMapper.toDto(peerCompany);
        var returnedPeerCompanyDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(peerCompanyDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(PeerCompanyDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the PeerCompany in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPeerCompany = peerCompanyMapper.toEntity(returnedPeerCompanyDTO);
        assertPeerCompanyUpdatableFieldsEquals(returnedPeerCompany, getPersistedPeerCompany(returnedPeerCompany));

        insertedPeerCompany = returnedPeerCompany;
    }

    @Test
    void createPeerCompanyWithExistingId() throws Exception {
        // Create the PeerCompany with an existing ID
        peerCompany.setId("existing_id");
        PeerCompanyDTO peerCompanyDTO = peerCompanyMapper.toDto(peerCompany);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(peerCompanyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PeerCompany in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkPeerSymbolIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        peerCompany.setPeerSymbol(null);

        // Create the PeerCompany, which fails.
        PeerCompanyDTO peerCompanyDTO = peerCompanyMapper.toDto(peerCompany);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(peerCompanyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllPeerCompanies() {
        // Initialize the database
        insertedPeerCompany = peerCompanyRepository.save(peerCompany).block();

        // Get all the peerCompanyList
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
            .value(hasItem(peerCompany.getId()))
            .jsonPath("$.[*].peerSymbol")
            .value(hasItem(DEFAULT_PEER_SYMBOL));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPeerCompaniesWithEagerRelationshipsIsEnabled() {
        when(peerCompanyServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(peerCompanyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPeerCompaniesWithEagerRelationshipsIsNotEnabled() {
        when(peerCompanyServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(peerCompanyRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPeerCompany() {
        // Initialize the database
        insertedPeerCompany = peerCompanyRepository.save(peerCompany).block();

        // Get the peerCompany
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, peerCompany.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(peerCompany.getId()))
            .jsonPath("$.peerSymbol")
            .value(is(DEFAULT_PEER_SYMBOL));
    }

    @Test
    void getNonExistingPeerCompany() {
        // Get the peerCompany
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPeerCompany() throws Exception {
        // Initialize the database
        insertedPeerCompany = peerCompanyRepository.save(peerCompany).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the peerCompany
        PeerCompany updatedPeerCompany = peerCompanyRepository.findById(peerCompany.getId()).block();
        updatedPeerCompany.peerSymbol(UPDATED_PEER_SYMBOL);
        PeerCompanyDTO peerCompanyDTO = peerCompanyMapper.toDto(updatedPeerCompany);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, peerCompanyDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(peerCompanyDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PeerCompany in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPeerCompanyToMatchAllProperties(updatedPeerCompany);
    }

    @Test
    void putNonExistingPeerCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        peerCompany.setId(UUID.randomUUID().toString());

        // Create the PeerCompany
        PeerCompanyDTO peerCompanyDTO = peerCompanyMapper.toDto(peerCompany);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, peerCompanyDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(peerCompanyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PeerCompany in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPeerCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        peerCompany.setId(UUID.randomUUID().toString());

        // Create the PeerCompany
        PeerCompanyDTO peerCompanyDTO = peerCompanyMapper.toDto(peerCompany);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(peerCompanyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PeerCompany in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPeerCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        peerCompany.setId(UUID.randomUUID().toString());

        // Create the PeerCompany
        PeerCompanyDTO peerCompanyDTO = peerCompanyMapper.toDto(peerCompany);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(peerCompanyDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PeerCompany in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePeerCompanyWithPatch() throws Exception {
        // Initialize the database
        insertedPeerCompany = peerCompanyRepository.save(peerCompany).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the peerCompany using partial update
        PeerCompany partialUpdatedPeerCompany = new PeerCompany();
        partialUpdatedPeerCompany.setId(peerCompany.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPeerCompany.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPeerCompany))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PeerCompany in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPeerCompanyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPeerCompany, peerCompany),
            getPersistedPeerCompany(peerCompany)
        );
    }

    @Test
    void fullUpdatePeerCompanyWithPatch() throws Exception {
        // Initialize the database
        insertedPeerCompany = peerCompanyRepository.save(peerCompany).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the peerCompany using partial update
        PeerCompany partialUpdatedPeerCompany = new PeerCompany();
        partialUpdatedPeerCompany.setId(peerCompany.getId());

        partialUpdatedPeerCompany.peerSymbol(UPDATED_PEER_SYMBOL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPeerCompany.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPeerCompany))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PeerCompany in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPeerCompanyUpdatableFieldsEquals(partialUpdatedPeerCompany, getPersistedPeerCompany(partialUpdatedPeerCompany));
    }

    @Test
    void patchNonExistingPeerCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        peerCompany.setId(UUID.randomUUID().toString());

        // Create the PeerCompany
        PeerCompanyDTO peerCompanyDTO = peerCompanyMapper.toDto(peerCompany);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, peerCompanyDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(peerCompanyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PeerCompany in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPeerCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        peerCompany.setId(UUID.randomUUID().toString());

        // Create the PeerCompany
        PeerCompanyDTO peerCompanyDTO = peerCompanyMapper.toDto(peerCompany);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(peerCompanyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PeerCompany in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPeerCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        peerCompany.setId(UUID.randomUUID().toString());

        // Create the PeerCompany
        PeerCompanyDTO peerCompanyDTO = peerCompanyMapper.toDto(peerCompany);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(peerCompanyDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PeerCompany in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePeerCompany() {
        // Initialize the database
        insertedPeerCompany = peerCompanyRepository.save(peerCompany).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the peerCompany
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, peerCompany.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return peerCompanyRepository.count().block();
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

    protected PeerCompany getPersistedPeerCompany(PeerCompany peerCompany) {
        return peerCompanyRepository.findById(peerCompany.getId()).block();
    }

    protected void assertPersistedPeerCompanyToMatchAllProperties(PeerCompany expectedPeerCompany) {
        assertPeerCompanyAllPropertiesEquals(expectedPeerCompany, getPersistedPeerCompany(expectedPeerCompany));
    }

    protected void assertPersistedPeerCompanyToMatchUpdatableProperties(PeerCompany expectedPeerCompany) {
        assertPeerCompanyAllUpdatablePropertiesEquals(expectedPeerCompany, getPersistedPeerCompany(expectedPeerCompany));
    }
}
