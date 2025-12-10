package com.stockapp.userservice.service.mapper;

import com.stockapp.userservice.domain.AppUser;
import com.stockapp.userservice.domain.PasswordHistory;
import com.stockapp.userservice.service.dto.AppUserDTO;
import com.stockapp.userservice.service.dto.PasswordHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PasswordHistory} and its DTO {@link PasswordHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface PasswordHistoryMapper extends EntityMapper<PasswordHistoryDTO, PasswordHistory> {
    @Mapping(target = "user", source = "user", qualifiedByName = "appUserLogin")
    PasswordHistoryDTO toDto(PasswordHistory s);

    @Named("appUserLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    AppUserDTO toDtoAppUserLogin(AppUser appUser);
}
