package com.meongmungi.controller;

import com.meongmungi.dto.PostRequestDto;
import com.meongmungi.dto.PostResponseDto;
import com.meongmungi.entity.CategoryType;
import com.meongmungi.entity.User;
import com.meongmungi.security.CustomOAuth2User;
import com.meongmungi.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 1) 게시글 생성
     */
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @Valid @RequestBody PostRequestDto requestDto
    ) {
        User currentUser = customUser.getUser();
        PostResponseDto created = postService.createPost(currentUser, requestDto);
        return ResponseEntity.ok(created);
    }

    /**
     * 2) 카테고리별 또는 전체 게시글 조회
     */
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts(
            @RequestParam(value = "category", required = false) CategoryType category
    ) {
        List<PostResponseDto> list = postService.getAllPosts(category);
        return ResponseEntity.ok(list);
    }

    /**
     * 3) 단일 게시글 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(
            @PathVariable("id") Long id
    ) {
        PostResponseDto dto = postService.getPostById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * 4) 게시글 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @PathVariable("id") Long id,
            @Valid @RequestBody PostRequestDto requestDto
    ) {
        User currentUser = customUser.getUser();
        PostResponseDto updated = postService.updatePost(currentUser, id, requestDto);
        return ResponseEntity.ok(updated);
    }

    /**
     * 5) 게시글 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            @PathVariable("id") Long id
    ) {
        User currentUser = customUser.getUser();
        postService.deletePost(currentUser, id);
        return ResponseEntity.noContent().build();
    }
}
