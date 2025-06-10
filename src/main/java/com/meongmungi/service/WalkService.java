package com.meongmungi.service;

import com.meongmungi.dto.WalkRequest;
import com.meongmungi.dto.WalkResponse;
import com.meongmungi.entity.Pet;
import com.meongmungi.entity.User;
import com.meongmungi.entity.Walk;
import com.meongmungi.repository.PetRepository;
import com.meongmungi.repository.UserRepository;
import com.meongmungi.repository.WalkRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WalkService {
    private final PetRepository    petRepository;
    private final WalkRepository   walkRepository;
    private final UserRepository userRepository;

    public WalkResponse createWalk(Long userId, Long petId, WalkRequest req) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));
        Pet pet = petRepository.findByIdAndOwner(petId, owner)
                .orElseThrow(() -> new EntityNotFoundException("펫을 찾을 수 없거나 소유자가 아닙니다."));
        Walk walk = Walk.builder()
                .pet(pet)
                .walkDate(req.getWalkDate())
                .calories(req.getCalories())
                .distanceKm(req.getDistanceKm())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .build();

        Walk saved = walkRepository.save(walk);
        return WalkResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<WalkResponse> listWalks(Long userId, Long petId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));
        Pet pet = petRepository.findByIdAndOwner(petId, owner)
                .orElseThrow(() -> new EntityNotFoundException("펫을 찾을 수 없거나 소유자가 아닙니다."));
        return walkRepository.findByPetId(pet.getId())
                .stream()
                .map(WalkResponse::fromEntity)
                .collect(Collectors.toList());
    }


    public void deleteWalk(Long userId, Long petId, Long walkId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));
        Pet pet = petRepository.findByIdAndOwner(petId, owner)
                .orElseThrow(() -> new EntityNotFoundException("펫을 찾을 수 없거나 소유자가 아닙니다."));
        Walk walk = walkRepository.findById(walkId)
                .orElseThrow(() -> new EntityNotFoundException("해당 산책 기록을 찾을 수 없습니다."));
        if (!walk.getPet().getId().equals(pet.getId())) {
            throw new EntityNotFoundException("해당 펫의 산책 기록이 아닙니다.");
        }
        walkRepository.delete(walk);
    }
}
