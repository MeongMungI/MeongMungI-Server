package com.meongmungi.community.controller;

import com.meongmungi.community.dto.ApiResponse;
import com.meongmungi.community.dto.PostDto;
import com.meongmungi.community.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community/posts")
@RequiredArgsConstructor
@Tag(name = "게시글 API", description = "게시글 관련 API")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<PostDto.Response>> createPost(@RequestBody PostDto.Request request) {
        PostDto.Response response = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("게시글이 성공적으로 생성되었습니다.", response));
    }

    @Operation(summary = "게시글 단일 조회", description = "ID로 게시글을 조회합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDto.Response>> getPost(
            @PathVariable Long postId,
            @RequestParam(required = false) Long userId) {
        PostDto.Response response = postService.getPostById(postId, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 정보와 댓글을 함께 조회합니다.")
    @GetMapping("/{postId}/details")
    public ResponseEntity<ApiResponse<PostDto.DetailResponse>> getPostWithComments(
            @PathVariable Long postId,
            @RequestParam(required = false) Long userId) {
        PostDto.DetailResponse response = postService.getPostWithComments(postId, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDto.Response>> updatePost(
            @PathVariable Long postId,
            @RequestBody PostDto.Request request,
            @RequestParam Long userId) {
        PostDto.Response response = postService.updatePost(postId, request, userId);
        return ResponseEntity.ok(ApiResponse.success("게시글이 성공적으로 수정되었습니다.", response));
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long postId,
            @RequestParam Long userId) {
        postService.deletePost(postId, userId);
        return ResponseEntity.ok(ApiResponse.success("게시글이 성공적으로 삭제되었습니다.", null));
    }

    @Operation(summary = "전체 게시글 조회", description = "전체 게시글을 페이지네이션으로 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostDto.Response>>> getAllPosts(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) Long userId) {
        Page<PostDto.Response> posts = postService.getAllPosts(pageable, userId);
        return ResponseEntity.ok(ApiResponse.success(posts));
    }

    @Operation(summary = "사용자 게시글 조회", description = "특정 사용자의 게시글을 조회합니다.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<PostDto.Response>>> getUserPosts(
            @PathVariable Long userId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<PostDto.Response> posts = postService.getUserPosts(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(posts));
    }

    @Operation(summary = "팔로잉 게시글 조회", description = "팔로우한 사용자의 게시글을 조회합니다.")
    @GetMapping("/following")
    public ResponseEntity<ApiResponse<Page<PostDto.Response>>> getFollowingPosts(
            @RequestParam Long userId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<PostDto.Response> posts = postService.getFollowingPosts(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(posts));
    }
}