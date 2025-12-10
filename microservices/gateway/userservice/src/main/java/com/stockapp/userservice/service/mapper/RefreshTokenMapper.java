package com.stockapp.userservice.service.mapper;

import com.stockapp.userservice.domain.AppUser;
import com.stockapp.userservice.domain.RefreshToken;
import com.stockapp.userservice.service.dto.AppUserDTO;
import com.stockapp.userservice.service.dto.RefreshTokenDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RefreshToken} and its DTO {@link RefreshTokenDTO}.
 */
@Mapper(componentModel = "spring")
public interface RefreshTokenMapper extends EntityMapper<RefreshTokenDTO, RefreshToken> {
    @Mapping(target = "user", source = "user", qualifiedByName = "appUserLogin")
    RefreshTokenDTO toDto(RefreshToken s);

    @Named("appUserLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    AppUserDTO toDtoAppUserLogin(AppUser appUser);
}
