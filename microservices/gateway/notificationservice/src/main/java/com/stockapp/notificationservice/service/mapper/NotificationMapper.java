package com.stockapp.notificationservice.service.mapper;

import com.stockapp.notificationservice.domain.Notification;
import com.stockapp.notificationservice.service.dto.NotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {}
