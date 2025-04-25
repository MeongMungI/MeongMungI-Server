package com.meongmungi.community.controller;

import com.meongmungi.community.dto.ApiResponse;
import com.meongmungi.community.dto.FollowDto;
import com.meongmungi.community.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community/follows")
@RequiredArgsConstructor
@Tag(name = "팔로우 API", description = "팔로우 관련 API")
public class FollowController {

    private final FollowService followService;

    @Operation(summary = "팔로우 추가", description = "사용자를 팔로우합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<FollowDto.Response>> followUser(
            @RequestBody FollowDto.Request request) {
        FollowDto.Response response = followService.followUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("팔로우가 성공적으로 추가되었습니다.", response));
    }

    @Operation(summary = "팔로우 취소", description = "사용자 팔로우를 취소합니다.")
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> unfollowUser(
            @RequestParam Long followerId,
            @RequestParam Long followingId) {
        followService.unfollowUser(followerId, followingId);
        return ResponseEntity.ok(ApiResponse.success("팔로우가 성공적으로 취소되었습니다.", null));
    }

    @Operation(summary = "팔로우 여부 확인", description = "사용자가 다른 사용자를 팔로우하는지 확인합니다.")
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> checkIfUserFollows(
            @RequestParam Long followerId,
            @RequestParam Long followingId) {
        boolean isFollowing = followService.checkIfUserFollows(followerId, followingId);
        return ResponseEntity.ok(ApiResponse.success(isFollowing));
    }

    @Operation(summary = "팔로잉 목록 조회", description = "사용자가 팔로우하는 사용자 목록을 조회합니다.")
    @GetMapping("/followings/{userId}")
    public ResponseEntity<ApiResponse<List<FollowDto.Response>>> getFollowings(
            @PathVariable Long userId) {
        List<FollowDto.Response> followings = followService.getFollowings(userId);
        return ResponseEntity.ok(ApiResponse.success(followings));
    }

    @Operation(summary = "팔로워 목록 조회", description = "사용자를 팔로우하는 사용자 목록을 조회합니다.")
    @GetMapping("/followers/{userId}")
    public ResponseEntity<ApiResponse<List<FollowDto.Response>>> getFollowers(
            @PathVariable Long userId) {
        List<FollowDto.Response> followers = followService.getFollowers(userId);
        return ResponseEntity.ok(ApiResponse.success(followers));
    }

    @Operation(summary = "팔로잉 수 조회", description = "사용자가 팔로우하는 사용자 수를 조회합니다.")
    @GetMapping("/followings/{userId}/count")
    public ResponseEntity<ApiResponse<Integer>> getFollowingCount(
            @PathVariable Long userId) {
        int count = followService.getFollowingCount(userId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    @Operation(summary = "팔로워 수 조회", description = "사용자를 팔로우하는 사용자 수를 조회합니다.")
    @GetMapping("/followers/{userId}/count")
    public ResponseEntity<ApiResponse<Integer>> getFollowerCount(
            @PathVariable Long userId) {
        int count = followService.getFollowerCount(userId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }
}