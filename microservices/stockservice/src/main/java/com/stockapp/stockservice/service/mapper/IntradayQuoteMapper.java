package com.stockapp.stockservice.service.mapper;

import com.stockapp.stockservice.domain.IntradayQuote;
import com.stockapp.stockservice.service.dto.IntradayQuoteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IntradayQuote} and its DTO {@link IntradayQuoteDTO}.
 */
@Mapper(componentModel = "spring")
public interface IntradayQuoteMapper extends EntityMapper<IntradayQuoteDTO, IntradayQuote> {}
