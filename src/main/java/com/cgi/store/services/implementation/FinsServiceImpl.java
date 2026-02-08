package com.cgi.store.services.implementation;

import java.util.List;

import org.springframework.stereotype.Service;
import com.cgi.store.dto.request.FinsDtoRequest;
import com.cgi.store.dto.response.FinsDtoResponse;
import com.cgi.store.entities.FinsEntity;
import com.cgi.store.mapper.FinsMapper;
import com.cgi.store.exceptions.ResourceNotFoundException;
import com.cgi.store.repository.FinsRepository;
import com.cgi.store.services.FinsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class FinsServiceImpl implements FinsService {

    private final FinsRepository finsRepository;
    private final FinsMapper finsMapper;

    @Override
    public List<FinsDtoResponse> getAllFins() {
        log.info("Getting all fins from Database");
        return finsMapper.toDtoResponseList(finsRepository.findAll());
    }

    @Override
    public FinsDtoResponse getFinsById(Long id) {
        log.info("Getting fin by id: {} from Database", id);
        return finsMapper.toDto(finsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Fin", id)));
    }
    
    @Override
    public FinsDtoResponse createFins(FinsDtoRequest finsDto) {
        log.info("Creating fin: {} in Database", finsDto);
        return finsMapper.toDto(finsRepository.save(finsMapper.toEntity(finsDto)));
    }

    @Override
    public FinsDtoResponse updateFins(Long id, FinsDtoRequest finsDto) {
        log.info("Updating fin with id: {} and finsDto: {} in Database", id, finsDto);
        FinsEntity existingFin = finsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Fin", id));
        existingFin = finsMapper.updateEntity(existingFin, finsDto);
        
        return finsMapper.toDto(finsRepository.save(existingFin));
    }

    @Override
    public void deleteFins(Long id) {
        log.info("Deleting fin by id: {} from Database", id);
        finsRepository.deleteById(id);
    }
}
