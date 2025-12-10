package com.stockapp.stockservice.service.mapper;

import com.stockapp.stockservice.domain.Company;
import com.stockapp.stockservice.domain.HistoricalPrice;
import com.stockapp.stockservice.service.dto.CompanyDTO;
import com.stockapp.stockservice.service.dto.HistoricalPriceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HistoricalPrice} and its DTO {@link HistoricalPriceDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistoricalPriceMapper extends EntityMapper<HistoricalPriceDTO, HistoricalPrice> {
    @Mapping(target = "company", source = "company", qualifiedByName = "companySymbol")
    HistoricalPriceDTO toDto(HistoricalPrice s);

    @Named("companySymbol")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "symbol", source = "symbol")
    CompanyDTO toDtoCompanySymbol(Company company);
}
