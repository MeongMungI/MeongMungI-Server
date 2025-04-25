package com.meongmungi.community.controller;

import com.meongmungi.community.dto.ApiResponse;
import com.meongmungi.community.dto.CommentDto;
import com.meongmungi.community.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community/comments")
@RequiredArgsConstructor
@Tag(name = "댓글 API", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "게시글에 새로운 댓글을 생성합니다.")
    @PostMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<CommentDto.Response>> createComment(
            @PathVariable Long postId,
            @RequestBody CommentDto.Request request) {
        CommentDto.Response response = commentService.createComment(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("댓글이 성공적으로 생성되었습니다.", response));
    }

    @Operation(summary = "게시글 댓글 조회", description = "특정 게시글의 모든 댓글을 조회합니다.")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<List<CommentDto.Response>>> getCommentsByPostId(
            @PathVariable Long postId) {
        List<CommentDto.Response> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }

    @Operation(summary = "게시글 댓글 페이지 조회", description = "특정 게시글의 댓글을 페이지네이션으로 조회합니다.")
    @GetMapping("/posts/{postId}/paged")
    public ResponseEntity<ApiResponse<Page<CommentDto.Response>>> getCommentsByPostIdPaginated(
            @PathVariable Long postId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<CommentDto.Response> comments = commentService.getCommentsByPostIdPaginated(postId, pageable);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }

    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentDto.Response>> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentDto.Request request,
            @RequestParam Long userId) {
        CommentDto.Response response = commentService.updateComment(commentId, request, userId);
        return ResponseEntity.ok(ApiResponse.success("댓글이 성공적으로 수정되었습니다.", response));
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long commentId,
            @RequestParam Long userId) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok(ApiResponse.success("댓글이 성공적으로 삭제되었습니다.", null));
    }
}