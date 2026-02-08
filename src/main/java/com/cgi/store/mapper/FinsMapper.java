package com.cgi.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

import com.cgi.store.dto.request.FinsDtoRequest;
import com.cgi.store.dto.response.FinsDtoResponse;
import com.cgi.store.entities.FinsEntity;

@Mapper(componentModel = "spring")
public interface FinsMapper {

    FinsMapper INSTANCE = Mappers.getMapper(FinsMapper.class);

    FinsDtoResponse toDto(FinsEntity entity);

    @Mapping(target = "id", ignore = true)
    FinsDtoResponse toDto(FinsDtoRequest dto);

    @Mapping(target = "id", ignore = true)
    FinsEntity updateEntity(@MappingTarget FinsEntity existingEntity, FinsDtoRequest dto);
    
    @Mapping(target = "id", ignore = true)
    FinsEntity toEntity(FinsDtoRequest dto);
    FinsEntity toEntity(FinsDtoResponse dto);

    List<FinsDtoResponse> toDtoResponseList(List<FinsEntity> entities);
    List<FinsEntity> toEntityResponseList(List<FinsDtoResponse> dtos);

    List<FinsEntity> toEntityRequestList(List<FinsDtoRequest> dtos);
    List<FinsDtoResponse> toDtoRequestList(List<FinsDtoRequest> dtos);
}
