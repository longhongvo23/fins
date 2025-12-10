package com.stockapp.stockservice.service.mapper;

import com.stockapp.stockservice.domain.Company;
import com.stockapp.stockservice.domain.Recommendation;
import com.stockapp.stockservice.service.dto.CompanyDTO;
import com.stockapp.stockservice.service.dto.RecommendationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Recommendation} and its DTO {@link RecommendationDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecommendationMapper extends EntityMapper<RecommendationDTO, Recommendation> {
    @Mapping(target = "company", source = "company", qualifiedByName = "companySymbol")
    RecommendationDTO toDto(Recommendation s);

    @Named("companySymbol")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "symbol", source = "symbol")
    CompanyDTO toDtoCompanySymbol(Company company);
}
