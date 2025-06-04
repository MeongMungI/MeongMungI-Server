package com.meongmungi.controller;

import com.meongmungi.dto.LikeResponseDto;
import com.meongmungi.entity.User;
import com.meongmungi.security.CustomOAuth2User;
import com.meongmungi.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    /**
     * 1) 게시글 좋아요 누르기
     */
    @PostMapping("/api/posts/{postId}/likes")
    public ResponseEntity<LikeResponseDto> likePost(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @PathVariable("postId") Long postId
    ) {
        User currentUser = customUser.getUser();
        LikeResponseDto response = likeService.likePost(currentUser, postId);
        return ResponseEntity.ok(response);
    }

    /**
     * 2) 게시글 좋아요 취소하기
     */
    @DeleteMapping("/api/posts/{postId}/likes")
    public ResponseEntity<LikeResponseDto> unlikePost(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @PathVariable("postId") Long postId
    ) {
        User currentUser = customUser.getUser();
        LikeResponseDto response = likeService.unlikePost(currentUser, postId);
        return ResponseEntity.ok(response);
    }

    /**
     * 3) 게시글 좋아요 상태 조회
     */
    @GetMapping("/api/posts/{postId}/likes")
    public ResponseEntity<LikeResponseDto> getLikeStatus(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @PathVariable("postId") Long postId
    ) {
        User currentUser = customUser.getUser();
        LikeResponseDto response = likeService.getLikeStatus(currentUser, postId);
        return ResponseEntity.ok(response);
    }
}