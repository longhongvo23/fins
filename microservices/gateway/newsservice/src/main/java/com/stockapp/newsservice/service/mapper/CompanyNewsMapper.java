package com.stockapp.newsservice.service.mapper;

import com.stockapp.newsservice.domain.CompanyNews;
import com.stockapp.newsservice.domain.CompanyRef;
import com.stockapp.newsservice.service.dto.CompanyNewsDTO;
import com.stockapp.newsservice.service.dto.CompanyRefDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompanyNews} and its DTO {@link CompanyNewsDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyNewsMapper extends EntityMapper<CompanyNewsDTO, CompanyNews> {
    @Mapping(target = "company", source = "company", qualifiedByName = "companyRefSymbol")
    CompanyNewsDTO toDto(CompanyNews s);

    @Named("companyRefSymbol")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "symbol", source = "symbol")
    CompanyRefDTO toDtoCompanyRefSymbol(CompanyRef companyRef);
}
