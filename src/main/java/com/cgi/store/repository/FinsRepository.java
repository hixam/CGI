package com.cgi.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cgi.store.entities.FinsEntity;

@Repository
public interface FinsRepository extends JpaRepository<FinsEntity, Long> {
    
}
