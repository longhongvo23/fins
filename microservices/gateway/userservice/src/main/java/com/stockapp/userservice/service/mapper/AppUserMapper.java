package com.stockapp.userservice.service.mapper;

import com.stockapp.userservice.domain.AppUser;
import com.stockapp.userservice.domain.Authority;
import com.stockapp.userservice.service.dto.AppUserDTO;
import com.stockapp.userservice.service.dto.AuthorityDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppUser} and its DTO {@link AppUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppUserMapper extends EntityMapper<AppUserDTO, AppUser> {
    @Mapping(target = "authorities", source = "authorities", qualifiedByName = "authorityNameSet")
    AppUserDTO toDto(AppUser s);

    @Mapping(target = "removeAuthorities", ignore = true)
    AppUser toEntity(AppUserDTO appUserDTO);

    @Named("authorityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    AuthorityDTO toDtoAuthorityName(Authority authority);

    @Named("authorityNameSet")
    default Set<AuthorityDTO> toDtoAuthorityNameSet(Set<Authority> authority) {
        return authority.stream().map(this::toDtoAuthorityName).collect(Collectors.toSet());
    }
}
