package com.cgi.store.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cgi.store.dto.request.GlassesDtoRequest;
import com.cgi.store.dto.response.GlassesDtoResponse;
import com.cgi.store.services.GlassesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Glasses", description = "API for managing glasses")
public class GlassesController {

    private final GlassesService glassesService;
    
    @Operation(summary = "Get all glasses")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all glasses"),
        @ApiResponse(responseCode = "500", description = "Internal server error (ErrorResponse: error, message)")
    })
    @GetMapping("/glasses")
    public ResponseEntity<List<GlassesDtoResponse>> getAllGlasses() {
        return ResponseEntity.ok(glassesService.getAllGlasses());
    }

    @Operation(summary = "Create a new glass")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully created a new glass"),
        @ApiResponse(responseCode = "400", description = "Bad request - validation failed (ErrorResponse: error, message)"),
        @ApiResponse(responseCode = "500", description = "Internal server error (ErrorResponse: error, message)")
    })
    @PostMapping("/glasses")
    public ResponseEntity<GlassesDtoResponse> createGlasses(@RequestBody @Valid GlassesDtoRequest glassesDto) {
        return ResponseEntity.ok(glassesService.createGlasses(glassesDto));
    }

    @Operation(summary = "Get a glass by id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved a glass by id"),
        @ApiResponse(responseCode = "404", description = "Not found - glass does not exist (ErrorResponse: error, message)"),
        @ApiResponse(responseCode = "500", description = "Internal server error (ErrorResponse: error, message)")
    })
    @GetMapping("/glasses/{id}")
    public ResponseEntity<GlassesDtoResponse> getGlassesById(@PathVariable Long id) {
        return ResponseEntity.ok(glassesService.getGlassesById(id));
    }
    
    @Operation(summary = "Update a glass by id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully updated a glass by id"),
        @ApiResponse(responseCode = "400", description = "Bad request - validation failed (ErrorResponse: error, message)"),
        @ApiResponse(responseCode = "404", description = "Not found - glass does not exist (ErrorResponse: error, message)"),
        @ApiResponse(responseCode = "500", description = "Internal server error (ErrorResponse: error, message)")
    })
    @PutMapping("/glasses/{id}")
    public ResponseEntity<GlassesDtoResponse> updateGlasses(@PathVariable Long id, @RequestBody @Valid GlassesDtoRequest glassesDto) {
        return ResponseEntity.ok(glassesService.updateGlasses(id, glassesDto));
    }

    @Operation(summary = "Delete a glass by id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully deleted a glass by id"),
        @ApiResponse(responseCode = "500", description = "Internal server error (ErrorResponse: error, message)")
    })
    @DeleteMapping("/glasses/{id}")
    public ResponseEntity<Void> deleteGlasses(@PathVariable Long id) {
        glassesService.deleteGlasses(id);
        return ResponseEntity.ok().build();
    }
}
