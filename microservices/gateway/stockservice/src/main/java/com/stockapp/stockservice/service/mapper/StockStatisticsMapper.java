package com.stockapp.stockservice.service.mapper;

import com.stockapp.stockservice.domain.StockStatistics;
import com.stockapp.stockservice.service.dto.StockStatisticsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StockStatistics} and its DTO {@link StockStatisticsDTO}.
 */
@Mapper(componentModel = "spring")
public interface StockStatisticsMapper extends EntityMapper<StockStatisticsDTO, StockStatistics> {}
