package com.meongmungi.controller;

import com.meongmungi.dto.LikeResponseDto;
import com.meongmungi.security.CustomOAuth2User;
import com.meongmungi.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService likeService;

    @PostMapping("/api/comments/{commentId}/likes")
    public ResponseEntity<LikeResponseDto> likeComment(
            @AuthenticationPrincipal CustomOAuth2User user,
            @PathVariable Long commentId
    ) {
        return ResponseEntity.ok(
                likeService.likeComment(user.getUser(), commentId)
        );
    }

    @DeleteMapping("/api/comments/{commentId}/likes")
    public ResponseEntity<LikeResponseDto> unlikeComment(
            @AuthenticationPrincipal CustomOAuth2User user,
            @PathVariable Long commentId
    ) {
        return ResponseEntity.ok(
                likeService.unlikeComment(user.getUser(), commentId)
        );
    }

    @GetMapping("/api/comments/{commentId}/likes")
    public ResponseEntity<LikeResponseDto> getCommentLikeStatus(
            @AuthenticationPrincipal CustomOAuth2User user,
            @PathVariable Long commentId
    ) {
        return ResponseEntity.ok(
                likeService.getLikeStatus(user.getUser(), commentId)
        );
    }
}
