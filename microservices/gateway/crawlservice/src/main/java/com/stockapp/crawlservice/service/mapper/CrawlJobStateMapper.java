package com.stockapp.crawlservice.service.mapper;

import com.stockapp.crawlservice.domain.CrawlJobState;
import com.stockapp.crawlservice.service.dto.CrawlJobStateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CrawlJobState} and its DTO {@link CrawlJobStateDTO}.
 */
@Mapper(componentModel = "spring")
public interface CrawlJobStateMapper extends EntityMapper<CrawlJobStateDTO, CrawlJobState> {}
