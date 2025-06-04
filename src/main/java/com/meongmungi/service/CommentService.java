package com.meongmungi.service;

import com.meongmungi.dto.CommentRequestDto;
import com.meongmungi.dto.CommentResponseDto;
import com.meongmungi.entity.Comment;
import com.meongmungi.entity.Post;
import com.meongmungi.entity.User;
import com.meongmungi.repository.CommentRepository;
import com.meongmungi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    /**
     * 댓글 등록
     */
    @Transactional
    public CommentResponseDto createComment(User owner, Long postId, CommentRequestDto dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .owner(owner)
                .post(post)
                .build();

        Comment saved = commentRepository.save(comment);

        return mapToDto(saved);
    }

    /**
     * 해당 게시글의 모든 댓글 조회
     */
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));

        return commentRepository.findAllByPost(post).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public void deleteComment(User owner, Long commentId) {
        Comment comment = commentRepository.findByIdAndOwner(commentId, owner)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없거나, 권한이 없습니다. id=" + commentId));
        commentRepository.delete(comment);
    }

    private CommentResponseDto mapToDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .ownerId(comment.getOwner().getId())
                .ownerNickname(comment.getOwner().getNickname())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
