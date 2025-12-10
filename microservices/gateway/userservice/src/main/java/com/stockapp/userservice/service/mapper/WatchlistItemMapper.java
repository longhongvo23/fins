package com.stockapp.userservice.service.mapper;

import com.stockapp.userservice.domain.AppUser;
import com.stockapp.userservice.domain.WatchlistItem;
import com.stockapp.userservice.service.dto.AppUserDTO;
import com.stockapp.userservice.service.dto.WatchlistItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WatchlistItem} and its DTO {@link WatchlistItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface WatchlistItemMapper extends EntityMapper<WatchlistItemDTO, WatchlistItem> {
    @Mapping(target = "user", source = "user", qualifiedByName = "appUserLogin")
    WatchlistItemDTO toDto(WatchlistItem s);

    @Named("appUserLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    AppUserDTO toDtoAppUserLogin(AppUser appUser);
}
