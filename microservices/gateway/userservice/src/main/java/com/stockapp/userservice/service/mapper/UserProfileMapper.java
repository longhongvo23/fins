package com.stockapp.userservice.service.mapper;

import com.stockapp.userservice.domain.AppUser;
import com.stockapp.userservice.domain.UserProfile;
import com.stockapp.userservice.service.dto.AppUserDTO;
import com.stockapp.userservice.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserProfile} and its DTO {@link UserProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserProfileMapper extends EntityMapper<UserProfileDTO, UserProfile> {
    @Mapping(target = "user", source = "user", qualifiedByName = "appUserLogin")
    UserProfileDTO toDto(UserProfile s);

    @Named("appUserLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    AppUserDTO toDtoAppUserLogin(AppUser appUser);
}
