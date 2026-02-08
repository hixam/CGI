package com.cgi.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cgi.store.entities.GlassesEntity;

@Repository
public interface GlassesRepository extends JpaRepository<GlassesEntity, Long> {
    
}
