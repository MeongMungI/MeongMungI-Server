package com.meongmungi.controller;

import com.meongmungi.dto.CommentRequestDto;
import com.meongmungi.dto.CommentResponseDto;
import com.meongmungi.entity.User;
import com.meongmungi.security.CustomOAuth2User;
import com.meongmungi.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 1) 댓글 등록
     */
    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @PathVariable("postId") Long postId,
            @Valid @RequestBody CommentRequestDto requestDto
    ) {
        User currentUser = customUser.getUser();
        CommentResponseDto created = commentService.createComment(currentUser, postId, requestDto);
        return ResponseEntity.ok(created);
    }

    /**
     * 2) 게시글의 모든 댓글 조회
     */
    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getComments(
            @PathVariable("postId") Long postId
    ) {
        List<CommentResponseDto> list = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(list);
    }

    /**
     * 3) 댓글 삭제 (작성자 본인만 가능)
     */
    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @PathVariable("commentId") Long commentId
    ) {
        User currentUser = customUser.getUser();
        commentService.deleteComment(currentUser, commentId);
        return ResponseEntity.noContent().build();
    }
}