package com.meongmungi.controller;

import com.meongmungi.dto.WalkRequest;
import com.meongmungi.dto.WalkResponse;
import com.meongmungi.service.WalkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/pets/{petId}/walks")
@RequiredArgsConstructor
public class WalkController {
    private final WalkService walkService;

    @PostMapping
    public ResponseEntity<WalkResponse> createWalk(
            @PathVariable Long userId,
            @PathVariable Long petId,
            @RequestBody @Valid WalkRequest req
    ) {
        WalkResponse res = walkService.createWalk(userId, petId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping
    public ResponseEntity<List<WalkResponse>> listWalks(
            @PathVariable Long userId,
            @PathVariable Long petId
    ) {
        List<WalkResponse> list = walkService.listWalks(userId, petId);
        return ResponseEntity.ok(list);
    }


    @DeleteMapping("/{walkId}")
    public ResponseEntity<Void> deleteWalk(
            @PathVariable Long userId,
            @PathVariable Long petId,
            @PathVariable Long walkId
    ) {
        walkService.deleteWalk(userId, petId, walkId);
        return ResponseEntity.noContent().build();
    }
}