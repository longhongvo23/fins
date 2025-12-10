package com.stockapp.userservice.service.mapper;

import com.stockapp.userservice.domain.AppUser;
import com.stockapp.userservice.domain.NotificationSetting;
import com.stockapp.userservice.service.dto.AppUserDTO;
import com.stockapp.userservice.service.dto.NotificationSettingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NotificationSetting} and its DTO {@link NotificationSettingDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationSettingMapper extends EntityMapper<NotificationSettingDTO, NotificationSetting> {
    @Mapping(target = "user", source = "user", qualifiedByName = "appUserLogin")
    NotificationSettingDTO toDto(NotificationSetting s);

    @Named("appUserLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    AppUserDTO toDtoAppUserLogin(AppUser appUser);
}
