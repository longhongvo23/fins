package com.stockapp.userservice.service.mapper;

import com.stockapp.userservice.domain.Authority;
import com.stockapp.userservice.service.dto.AuthorityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Authority} and its DTO {@link AuthorityDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuthorityMapper extends EntityMapper<AuthorityDTO, Authority> {}
