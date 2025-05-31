package com.meongmungi.repository;

import com.meongmungi.entity.Pet;
import com.meongmungi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findAllByOwner(User owner);

    // 특정유저 + Pet
    Optional<Pet> findByIdAndOwner(Long id, User owner);
}
