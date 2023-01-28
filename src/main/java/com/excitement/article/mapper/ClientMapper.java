package com.excitement.article.mapper;

import com.excitement.article.contoller.dto.ClientDto;
import com.excitement.article.repository.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    @Mapping(target = "id", ignore = true)
    Client toModel(ClientDto dto);
}
