package com.stockapp.userservice.service.mapper;

import com.stockapp.userservice.domain.AppUser;
import com.stockapp.userservice.domain.Session;
import com.stockapp.userservice.service.dto.AppUserDTO;
import com.stockapp.userservice.service.dto.SessionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Session} and its DTO {@link SessionDTO}.
 */
@Mapper(componentModel = "spring")
public interface SessionMapper extends EntityMapper<SessionDTO, Session> {
    @Mapping(target = "user", source = "user", qualifiedByName = "appUserLogin")
    SessionDTO toDto(Session s);

    @Named("appUserLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    AppUserDTO toDtoAppUserLogin(AppUser appUser);
}
