package com.cgi.store.services.implementation;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.cgi.store.dto.request.GlassesDtoRequest;
import com.cgi.store.dto.response.GlassesDtoResponse;
import com.cgi.store.entities.GlassesEntity;
import com.cgi.store.mapper.GlassesMapper;
import com.cgi.store.exceptions.ResourceNotFoundException;
import com.cgi.store.repository.GlassesRepository;
import com.cgi.store.services.GlassesService;

@Service
@RequiredArgsConstructor
@Slf4j
public class GlassesServiceImpl implements GlassesService {

    private final GlassesRepository glassesRepository;
    private final GlassesMapper glassesMapper;

    @Override
    public List<GlassesDtoResponse> getAllGlasses() {
        log.info("Getting all glasses");
        return glassesMapper.toDtoResponseList(glassesRepository.findAll());
    }

    @Override
    public GlassesDtoResponse getGlassesById(Long id) {
        log.info("Getting glass by id: {} from Database", id);
        return glassesMapper
                .toDto(glassesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Glass", id)));
    }

    @Override
    public GlassesDtoResponse createGlasses(GlassesDtoRequest glassesDto) {
        log.info("Creating glass: {} in Database", glassesDto);
        return glassesMapper.toDto(glassesRepository.save(glassesMapper.toEntity(glassesDto)));
    }

    @Override
    public GlassesDtoResponse updateGlasses(Long id, GlassesDtoRequest glassesDto) {
        log.info("Updating glass with id: {} and glassesDto: {} in Database", id, glassesDto);
        GlassesEntity existingGlass = glassesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Glass", id));
        existingGlass = glassesMapper.updateEntity(existingGlass, glassesDto);

        return glassesMapper.toDto(glassesRepository.save(existingGlass));
    }

    @Override
    public void deleteGlasses(Long id) {
        log.info("Deleting glass by id: {} from Database", id);
        glassesRepository.deleteById(id);
    }
}