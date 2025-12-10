package com.stockapp.stockservice.service.mapper;

import com.stockapp.stockservice.domain.Company;
import com.stockapp.stockservice.domain.PeerCompany;
import com.stockapp.stockservice.service.dto.CompanyDTO;
import com.stockapp.stockservice.service.dto.PeerCompanyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PeerCompany} and its DTO {@link PeerCompanyDTO}.
 */
@Mapper(componentModel = "spring")
public interface PeerCompanyMapper extends EntityMapper<PeerCompanyDTO, PeerCompany> {
    @Mapping(target = "company", source = "company", qualifiedByName = "companySymbol")
    PeerCompanyDTO toDto(PeerCompany s);

    @Named("companySymbol")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "symbol", source = "symbol")
    CompanyDTO toDtoCompanySymbol(Company company);
}
