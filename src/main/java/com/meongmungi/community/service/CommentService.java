package com.meongmungi.community.service;

import com.meongmungi.community.domain.Comment;
import com.meongmungi.community.domain.Post;
import com.meongmungi.community.dto.CommentDto;
import com.meongmungi.community.repository.CommentRepository;
import com.meongmungi.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentDto.Response createComment(Long postId, CommentDto.Request request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .userId(request.getUserId())
                .userName(request.getUserName())
                .userProfileImage(request.getUserProfileImage())
                .post(post)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentDto.Response.fromEntity(savedComment);
    }

    public List<CommentDto.Response> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);

        return comments.stream()
                .map(CommentDto.Response::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<CommentDto.Response> getCommentsByPostIdPaginated(Long postId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId, pageable);

        return comments.map(CommentDto.Response::fromEntity);
    }

    @Transactional
    public CommentDto.Response updateComment(Long commentId, CommentDto.Request request, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("댓글 작성자만 수정할 수 있습니다.");
        }

        Comment updatedComment = Comment.builder()
                .id(comment.getId())
                .content(request.getContent())
                .userId(comment.getUserId())
                .userName(comment.getUserName())
                .userProfileImage(comment.getUserProfileImage())
                .post(comment.getPost())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();

        Comment savedComment = commentRepository.save(updatedComment);
        return CommentDto.Response.fromEntity(savedComment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("댓글 작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }
}