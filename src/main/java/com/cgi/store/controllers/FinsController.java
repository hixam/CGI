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

import com.cgi.store.dto.FinsDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class FinsController {

    @GetMapping("/fins")
    public ResponseEntity<List<FinsDto>> getAllFins() {
        return ResponseEntity.ok(List.of(null));
    }

    @PostMapping("/fins")
    public ResponseEntity<FinsDto> createFins(@RequestBody @Valid FinsDto finsDto) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/fins/{id}")
    public ResponseEntity<FinsDto> getFinsById(@PathVariable Long id) {
        return ResponseEntity.ok(null);
    }

    @PutMapping("/fins/{id}")
    public ResponseEntity<FinsDto> updateFins(@PathVariable Long id, @RequestBody @Valid FinsDto finsDto) {
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/fins/{id}")
    public ResponseEntity<Void> deleteFins(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}
