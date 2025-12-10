package com.stockapp.userservice.service;

import com.stockapp.userservice.repository.NotificationSettingRepository;
import com.stockapp.userservice.service.dto.NotificationSettingDTO;
import com.stockapp.userservice.service.mapper.NotificationSettingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.stockapp.userservice.domain.NotificationSetting}.
 */
@Service
public class NotificationSettingService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationSettingService.class);

    private final NotificationSettingRepository notificationSettingRepository;

    private final NotificationSettingMapper notificationSettingMapper;

    public NotificationSettingService(
        NotificationSettingRepository notificationSettingRepository,
        NotificationSettingMapper notificationSettingMapper
    ) {
        this.notificationSettingRepository = notificationSettingRepository;
        this.notificationSettingMapper = notificationSettingMapper;
    }

    /**
     * Save a notificationSetting.
     *
     * @param notificationSettingDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<NotificationSettingDTO> save(NotificationSettingDTO notificationSettingDTO) {
        LOG.debug("Request to save NotificationSetting : {}", notificationSettingDTO);
        return notificationSettingRepository
            .save(notificationSettingMapper.toEntity(notificationSettingDTO))
            .map(notificationSettingMapper::toDto);
    }

    /**
     * Update a notificationSetting.
     *
     * @param notificationSettingDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<NotificationSettingDTO> update(NotificationSettingDTO notificationSettingDTO) {
        LOG.debug("Request to update NotificationSetting : {}", notificationSettingDTO);
        return notificationSettingRepository
            .save(notificationSettingMapper.toEntity(notificationSettingDTO))
            .map(notificationSettingMapper::toDto);
    }

    /**
     * Partially update a notificationSetting.
     *
     * @param notificationSettingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<NotificationSettingDTO> partialUpdate(NotificationSettingDTO notificationSettingDTO) {
        LOG.debug("Request to partially update NotificationSetting : {}", notificationSettingDTO);

        return notificationSettingRepository
            .findById(notificationSettingDTO.getId())
            .map(existingNotificationSetting -> {
                notificationSettingMapper.partialUpdate(existingNotificationSetting, notificationSettingDTO);

                return existingNotificationSetting;
            })
            .flatMap(notificationSettingRepository::save)
            .map(notificationSettingMapper::toDto);
    }

    /**
     * Get all the notificationSettings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<NotificationSettingDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all NotificationSettings");
        return notificationSettingRepository.findAllBy(pageable).map(notificationSettingMapper::toDto);
    }

    /**
     * Get all the notificationSettings with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<NotificationSettingDTO> findAllWithEagerRelationships(Pageable pageable) {
        return notificationSettingRepository.findAllWithEagerRelationships(pageable).map(notificationSettingMapper::toDto);
    }

    /**
     * Returns the number of notificationSettings available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return notificationSettingRepository.count();
    }

    /**
     * Get one notificationSetting by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<NotificationSettingDTO> findOne(String id) {
        LOG.debug("Request to get NotificationSetting : {}", id);
        return notificationSettingRepository.findOneWithEagerRelationships(id).map(notificationSettingMapper::toDto);
    }

    /**
     * Delete the notificationSetting by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        LOG.debug("Request to delete NotificationSetting : {}", id);
        return notificationSettingRepository.deleteById(id);
    }
}
