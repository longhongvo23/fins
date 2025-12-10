package com.stockapp.newsservice.service.mapper;

import com.stockapp.newsservice.domain.CompanyRef;
import com.stockapp.newsservice.service.dto.CompanyRefDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompanyRef} and its DTO {@link CompanyRefDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyRefMapper extends EntityMapper<CompanyRefDTO, CompanyRef> {}
