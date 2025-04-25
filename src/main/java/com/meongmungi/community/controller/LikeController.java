package com.meongmungi.community.controller;

import com.meongmungi.community.dto.ApiResponse;
import com.meongmungi.community.dto.LikeDto;
import com.meongmungi.community.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community/likes")
@RequiredArgsConstructor
@Tag(name = "좋아요 API", description = "좋아요 관련 API")
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "좋아요 추가", description = "게시글에 좋아요를 추가합니다.")
    @PostMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<LikeDto.Response>> addLike(
            @PathVariable Long postId,
            @RequestBody LikeDto.Request request) {
        LikeDto.Response response = likeService.addLike(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("좋아요가 성공적으로 추가되었습니다.", response));
    }

    @Operation(summary = "좋아요 취소", description = "게시글에서 좋아요를 취소합니다.")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Void>> removeLike(
            @PathVariable Long postId,
            @RequestParam Long userId) {
        likeService.removeLike(postId, userId);
        return ResponseEntity.ok(ApiResponse.success("좋아요가 성공적으로 취소되었습니다.", null));
    }

    @Operation(summary = "좋아요 여부 확인", description = "사용자가 게시글에 좋아요를 눌렀는지 확인합니다.")
    @GetMapping("/posts/{postId}/check")
    public ResponseEntity<ApiResponse<Boolean>> checkIfUserLikedPost(
            @PathVariable Long postId,
            @RequestParam Long userId) {
        boolean isLiked = likeService.checkIfUserLikedPost(postId, userId);
        return ResponseEntity.ok(ApiResponse.success(isLiked));
    }

    @Operation(summary = "좋아요 수 조회", description = "게시글의 좋아요 수를 조회합니다.")
    @GetMapping("/posts/{postId}/count")
    public ResponseEntity<ApiResponse<Integer>> getLikeCount(
            @PathVariable Long postId) {
        int count = likeService.getLikeCount(postId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }
}