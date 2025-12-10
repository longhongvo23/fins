package com.stockapp.stockservice.service.mapper;

import com.stockapp.stockservice.domain.Company;
import com.stockapp.stockservice.domain.HistoricalPrice;
import com.stockapp.stockservice.service.dto.CompanyDTO;
import com.stockapp.stockservice.service.dto.HistoricalPriceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HistoricalPrice} and its DTO
 * {@link HistoricalPriceDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistoricalPriceMapper extends EntityMapper<HistoricalPriceDTO, HistoricalPrice> {
    @Mapping(target = "company", source = "company", qualifiedByName = "companySymbol")
    HistoricalPriceDTO toDto(HistoricalPrice s);

    @Mapping(target = "company", source = "company", qualifiedByName = "companyFromId")
    HistoricalPrice toEntity(HistoricalPriceDTO dto);

    @Named("companySymbol")
    CompanyDTO toDtoCompanySymbol(Company company);

    @Named("companyFromId")
    @Mapping(target = "historicalPrices", ignore = true)
    @Mapping(target = "removeHistoricalPrices", ignore = true)
    @Mapping(target = "financials", ignore = true)
    @Mapping(target = "removeFinancials", ignore = true)
    @Mapping(target = "recommendations", ignore = true)
    @Mapping(target = "removeRecommendations", ignore = true)
    @Mapping(target = "peers", ignore = true)
    @Mapping(target = "removePeers", ignore = true)
    Company companyFromId(CompanyDTO companyDTO);
}
