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

import com.cgi.store.dto.GlassesDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class GlassesController {
    
    @GetMapping("/glasses")
    public ResponseEntity<List<GlassesDto>> getAllGlasses() {
        return ResponseEntity.ok(List.of(null));
    }

    @PostMapping("/glasses")
    public ResponseEntity<GlassesDto> createGlasses(@RequestBody @Valid GlassesDto glassesDto) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/glasses/{id}")
    public ResponseEntity<GlassesDto> getGlassesById(@PathVariable Long id) {
        return ResponseEntity.ok(null);
    }
    
    @PutMapping("/glasses/{id}")
    public ResponseEntity<GlassesDto> updateGlasses(@PathVariable Long id, @RequestBody @Valid GlassesDto glassesDto) {
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/glasses/{id}")
    public ResponseEntity<Void> deleteGlasses(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}
