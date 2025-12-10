package com.stockapp.stockservice.service.mapper;

import com.stockapp.stockservice.domain.Company;
import com.stockapp.stockservice.service.dto.CompanyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {}
