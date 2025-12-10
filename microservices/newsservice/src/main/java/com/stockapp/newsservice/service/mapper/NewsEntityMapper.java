package com.stockapp.newsservice.service.mapper;

import com.stockapp.newsservice.domain.CompanyNews;
import com.stockapp.newsservice.domain.NewsEntity;
import com.stockapp.newsservice.service.dto.CompanyNewsDTO;
import com.stockapp.newsservice.service.dto.NewsEntityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NewsEntity} and its DTO {@link NewsEntityDTO}.
 */
@Mapper(componentModel = "spring")
public interface NewsEntityMapper extends EntityMapper<NewsEntityDTO, NewsEntity> {
    @Mapping(target = "news", source = "news", qualifiedByName = "companyNewsUuid")
    NewsEntityDTO toDto(NewsEntity s);

    @Named("companyNewsUuid")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "uuid", source = "uuid")
    CompanyNewsDTO toDtoCompanyNewsUuid(CompanyNews companyNews);
}
