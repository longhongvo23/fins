package com.stockapp.stockservice.service.mapper;

import com.stockapp.stockservice.domain.Company;
import com.stockapp.stockservice.domain.Financial;
import com.stockapp.stockservice.service.dto.CompanyDTO;
import com.stockapp.stockservice.service.dto.FinancialDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Financial} and its DTO {@link FinancialDTO}.
 */
@Mapper(componentModel = "spring")
public interface FinancialMapper extends EntityMapper<FinancialDTO, Financial> {
    @Mapping(target = "company", source = "company", qualifiedByName = "companySymbol")
    FinancialDTO toDto(Financial s);

    @Named("companySymbol")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "symbol", source = "symbol")
    CompanyDTO toDtoCompanySymbol(Company company);
}
