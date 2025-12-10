package com.stockapp.userservice.service;

import com.stockapp.userservice.repository.AppUserRepository;
import com.stockapp.userservice.service.dto.AppUserDTO;
import com.stockapp.userservice.service.mapper.AppUserMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.stockapp.userservice.domain.AppUser}.
 */
@Service
public class AppUserService {

    private static final Logger LOG = LoggerFactory.getLogger(AppUserService.class);

    private final AppUserRepository appUserRepository;

    private final AppUserMapper appUserMapper;

    public AppUserService(AppUserRepository appUserRepository, AppUserMapper appUserMapper) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
    }

    /**
     * Save a appUser.
     *
     * @param appUserDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AppUserDTO> save(AppUserDTO appUserDTO) {
        LOG.debug("Request to save AppUser : {}", appUserDTO);
        return appUserRepository.save(appUserMapper.toEntity(appUserDTO)).map(appUserMapper::toDto);
    }

    /**
     * Update a appUser.
     *
     * @param appUserDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AppUserDTO> update(AppUserDTO appUserDTO) {
        LOG.debug("Request to update AppUser : {}", appUserDTO);
        return appUserRepository.save(appUserMapper.toEntity(appUserDTO)).map(appUserMapper::toDto);
    }

    /**
     * Partially update a appUser.
     *
     * @param appUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<AppUserDTO> partialUpdate(AppUserDTO appUserDTO) {
        LOG.debug("Request to partially update AppUser : {}", appUserDTO);

        return appUserRepository
            .findById(appUserDTO.getId())
            .map(existingAppUser -> {
                appUserMapper.partialUpdate(existingAppUser, appUserDTO);

                return existingAppUser;
            })
            .flatMap(appUserRepository::save)
            .map(appUserMapper::toDto);
    }

    /**
     * Get all the appUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<AppUserDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AppUsers");
        return appUserRepository.findAllBy(pageable).map(appUserMapper::toDto);
    }

    /**
     * Get all the appUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<AppUserDTO> findAllWithEagerRelationships(Pageable pageable) {
        return appUserRepository.findAllWithEagerRelationships(pageable).map(appUserMapper::toDto);
    }

    /**
     *  Get all the appUsers where Profile is {@code null}.
     *  @return the list of entities.
     */

    public List<AppUserDTO> findAllWhereProfileIsNull() {
        LOG.debug("Request to get all appUsers where Profile is null");
        return appUserRepository.findAll().filter(appUser -> appUser.getProfile() == null).map(appUserMapper::toDto);
    }

    /**
     *  Get all the appUsers where NotificationSetting is {@code null}.
     *  @return the list of entities.
     */

    public List<AppUserDTO> findAllWhereNotificationSettingIsNull() {
        LOG.debug("Request to get all appUsers where NotificationSetting is null");
        return appUserRepository.findAll().filter(appUser -> appUser.getNotificationSetting() == null).map(appUserMapper::toDto);
    }

    /**
     * Returns the number of appUsers available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return appUserRepository.count();
    }

    /**
     * Get one appUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<AppUserDTO> findOne(String id) {
        LOG.debug("Request to get AppUser : {}", id);
        return appUserRepository.findOneWithEagerRelationships(id).map(appUserMapper::toDto);
    }

    /**
     * Delete the appUser by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete AppUser : {}", id);
        return appUserRepository.deleteById(id);
    }
}
