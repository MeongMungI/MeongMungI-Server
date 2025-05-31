package com.meongmungi.controller;

import com.meongmungi.dto.PetRequestDto;
import com.meongmungi.dto.PetResponseDto;
import com.meongmungi.entity.User;
import com.meongmungi.security.CustomOAuth2User;
import com.meongmungi.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    public ResponseEntity<PetResponseDto> createPet(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @Valid @RequestBody PetRequestDto requestDto
    ) {
        User currentUser = customUser.getUser();
        PetResponseDto created = petService.createPet(currentUser, requestDto);
        return ResponseEntity.ok(created);
    }


    @GetMapping
    public ResponseEntity<List<PetResponseDto>> getAllPets(
            @AuthenticationPrincipal CustomOAuth2User customUser
    ) {
        User currentUser = customUser.getUser();
        List<PetResponseDto> list = petService.getAllPetsOfUser(currentUser);
        return ResponseEntity.ok(list);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PetResponseDto> getPetById(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @PathVariable("id") Long id
    ) {
        User currentUser = customUser.getUser();
        PetResponseDto dto = petService.getPetById(currentUser, id);
        return ResponseEntity.ok(dto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDto> updatePet(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @PathVariable("id") Long id,
            @Valid @RequestBody PetRequestDto requestDto
    ) {
        User currentUser = customUser.getUser();
        PetResponseDto updated = petService.updatePet(currentUser, id, requestDto);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @PathVariable("id") Long id
    ) {
        User currentUser = customUser.getUser();
        petService.deletePet(currentUser, id);
        return ResponseEntity.noContent().build();
    }
}
