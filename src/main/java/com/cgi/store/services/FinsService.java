package com.cgi.store.services;

import java.util.List;

import com.cgi.store.dto.request.FinsDtoRequest;
import com.cgi.store.dto.response.FinsDtoResponse;

public interface FinsService {

    List<FinsDtoResponse> getAllFins();
    FinsDtoResponse getFinsById(Long id);
    FinsDtoResponse createFins(FinsDtoRequest finsDto);
    FinsDtoResponse updateFins(Long id, FinsDtoRequest finsDto);
    void deleteFins(Long id);
    
}
