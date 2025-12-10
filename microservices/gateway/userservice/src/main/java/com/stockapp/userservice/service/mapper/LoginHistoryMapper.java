package com.stockapp.userservice.service.mapper;

import com.stockapp.userservice.domain.AppUser;
import com.stockapp.userservice.domain.LoginHistory;
import com.stockapp.userservice.service.dto.AppUserDTO;
import com.stockapp.userservice.service.dto.LoginHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LoginHistory} and its DTO {@link LoginHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface LoginHistoryMapper extends EntityMapper<LoginHistoryDTO, LoginHistory> {
    @Mapping(target = "user", source = "user", qualifiedByName = "appUserLogin")
    LoginHistoryDTO toDto(LoginHistory s);

    @Named("appUserLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    AppUserDTO toDtoAppUserLogin(AppUser appUser);
}
