package com.meongmungi.repository;

import com.meongmungi.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalkRepository extends JpaRepository<Walk, Long> {
    List<Walk> findByPetId(Long petId);
}
