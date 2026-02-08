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

import com.cgi.store.dto.request.FinsDtoRequest;
import com.cgi.store.dto.response.FinsDtoResponse;
import com.cgi.store.services.FinsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Fins", description = "API for managing fins")
public class FinsController {

    private final FinsService finsService;

    @Operation(summary = "Get all fins")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all fins"),
        @ApiResponse(responseCode = "500", description = "Internal server error (ErrorResponse: error, message)")
    })
    @GetMapping("/fins")
    public ResponseEntity<List<FinsDtoResponse>> getAllFins() {
        return ResponseEntity.ok(finsService.getAllFins());
    }


    @Operation(summary = "Create a new fin")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully created a new fin"),
        @ApiResponse(responseCode = "400", description = "Bad request - validation failed (ErrorResponse: error, message)"),
        @ApiResponse(responseCode = "500", description = "Internal server error (ErrorResponse: error, message)")
    })
    @PostMapping("/fins")
    public ResponseEntity<FinsDtoResponse> createFins(@RequestBody @Valid FinsDtoRequest finsDto) {
        return ResponseEntity.ok(finsService.createFins(finsDto));
    }

    @Operation(summary = "Get a fin by id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved a fin by id"),
        @ApiResponse(responseCode = "404", description = "Not found - fin does not exist (ErrorResponse: error, message)"),
        @ApiResponse(responseCode = "500", description = "Internal server error (ErrorResponse: error, message)")
    })
    @GetMapping("/fins/{id}")
    public ResponseEntity<FinsDtoResponse> getFinsById(@PathVariable Long id) {
        return ResponseEntity.ok(finsService.getFinsById(id));
    }

    @Operation(summary = "Update a fin by id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully updated a fin by id"),
        @ApiResponse(responseCode = "400", description = "Bad request - validation failed (ErrorResponse: error, message)"),
        @ApiResponse(responseCode = "404", description = "Not found - fin does not exist (ErrorResponse: error, message)"),
        @ApiResponse(responseCode = "500", description = "Internal server error (ErrorResponse: error, message)")
    })
    @PutMapping("/fins/{id}")
    public ResponseEntity<FinsDtoResponse> updateFins(@PathVariable Long id, @RequestBody @Valid FinsDtoRequest finsDto) {
        return ResponseEntity.ok(finsService.updateFins(id, finsDto));
    }

    @Operation(summary = "Delete a fin by id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully deleted a fin by id"),
        @ApiResponse(responseCode = "500", description = "Internal server error (ErrorResponse: error, message)")
    })
    @DeleteMapping("/fins/{id}")
    public ResponseEntity<Void> deleteFins(@PathVariable Long id) {
        finsService.deleteFins(id);
        return ResponseEntity.ok().build();
    }
}
