package com.cgi.store.services;

import java.util.List;

import com.cgi.store.dto.request.GlassesDtoRequest;
import com.cgi.store.dto.response.GlassesDtoResponse;

public interface GlassesService {

    List<GlassesDtoResponse> getAllGlasses();
    GlassesDtoResponse getGlassesById(Long id);
    GlassesDtoResponse createGlasses(GlassesDtoRequest glassesDto);
    GlassesDtoResponse updateGlasses(Long id, GlassesDtoRequest glassesDto);
    void deleteGlasses(Long id);
    
}
