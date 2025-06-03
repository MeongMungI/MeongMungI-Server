package com.meongmungi.service;

import com.meongmungi.dto.PetRequestDto;
import com.meongmungi.dto.PetResponseDto;
import com.meongmungi.entity.Pet;
import com.meongmungi.entity.User;
import com.meongmungi.repository.PetRepository;
import com.meongmungi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;


    @Transactional
    public PetResponseDto createPet(User owner, PetRequestDto dto) {
        Pet pet = Pet.builder()
                .name(dto.getName())
                .breed(dto.getBreed())
                .gender(dto.getGender())
                .weight(dto.getWeight())
                .birthDate(dto.getBirthDate())
                .photoUrl(dto.getPhotoUrl())
                .owner(owner)
                .build();

        Pet saved = petRepository.save(pet);
        return mapToDto(saved);
    }


    @Transactional(readOnly = true)
    public List<PetResponseDto> getAllPetsOfUser(User owner) {
        List<Pet> pets = petRepository.findAllByOwner(owner);
        return pets.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public PetResponseDto getPetById(User owner, Long petId) {
        Pet pet = petRepository.findByIdAndOwner(petId, owner)
                .orElseThrow(() -> new IllegalArgumentException("해당 반려견을 찾을 수 없거나, 권한이 없습니다."));
        return mapToDto(pet);
    }


    @Transactional
    public PetResponseDto updatePet(User owner, Long petId, PetRequestDto dto) {
        Pet pet = petRepository.findByIdAndOwner(petId, owner)
                .orElseThrow(() -> new IllegalArgumentException("해당 반려견을 찾을 수 없거나, 권한이 없습니다."));

        pet.setName(dto.getName());
        pet.setBreed(dto.getBreed());
        pet.setGender(dto.getGender());
        pet.setWeight(dto.getWeight());
        pet.setBirthDate(dto.getBirthDate());
        pet.setPhotoUrl(dto.getPhotoUrl());
        return mapToDto(pet);
    }


    @Transactional
    public void deletePet(User owner, Long petId) {
        Pet pet = petRepository.findByIdAndOwner(petId, owner)
                .orElseThrow(() -> new IllegalArgumentException("해당 반려견을 찾을 수 없거나, 권한이 없습니다."));
        petRepository.delete(pet);
    }


    private PetResponseDto mapToDto(Pet pet) {
        return PetResponseDto.builder()
                .id(pet.getId())
                .name(pet.getName())
                .breed(pet.getBreed())
                .gender(pet.getGender())
                .weight(pet.getWeight())
                .birthDate(pet.getBirthDate())
                .photoUrl(pet.getPhotoUrl())
                .build();
    }
}
