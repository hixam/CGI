package com.cgi.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

import com.cgi.store.dto.request.GlassesDtoRequest;
import com.cgi.store.dto.response.GlassesDtoResponse;
import com.cgi.store.entities.GlassesEntity;

@Mapper(componentModel = "spring")
public interface GlassesMapper {

    GlassesMapper INSTANCE = Mappers.getMapper(GlassesMapper.class);

    GlassesDtoResponse toDto(GlassesEntity entity);

    @Mapping(target = "id", ignore = true)
    GlassesDtoResponse toDto(GlassesDtoRequest dto);

    @Mapping(target = "id", ignore = true)
    GlassesEntity updateEntity(@MappingTarget GlassesEntity existingEntity, GlassesDtoRequest dto);

    GlassesEntity toEntity(GlassesDtoResponse dto);
    
    @Mapping(target = "id", ignore = true)
    GlassesEntity toEntity(GlassesDtoRequest dto);

    List<GlassesDtoResponse> toDtoResponseList(List<GlassesEntity> entities);
    List<GlassesEntity> toEntityResponseList(List<GlassesDtoResponse> dtos);

    List<GlassesEntity> toEntityRequestList(List<GlassesDtoRequest> dtos);
    List<GlassesDtoResponse> toDtoRequestList(List<GlassesDtoRequest> dtos);
}
