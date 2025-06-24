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

    @Transactional
    public CommentResponseDto createComment(User owner, Long postId, CommentRequestDto dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id=" + postId));

        Comment parent = null;
        if (dto.getParentCommentId() != null) {
            parent = commentRepository.findById(dto.getParentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 없습니다. id=" + dto.getParentCommentId()));
        }

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .owner(owner)
                .post(post)
                .parentComment(parent)
                .build();
        Comment saved = commentRepository.save(comment);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id=" + postId));

        return commentRepository.findAllByPostAndParentCommentIsNull(post)
                .stream()
                .map(this::mapToDtoWithReplies)
                .collect(Collectors.toList());
    }

    private CommentResponseDto mapToDtoWithReplies(Comment c) {
        CommentResponseDto dto = mapToDto(c);
        dto.setReplies(
                c.getReplies().stream()
                        .map(this::mapToDtoWithReplies)
                        .collect(Collectors.toList())
        );
        return dto;
    }

    private CommentResponseDto mapToDto(Comment c) {
        return CommentResponseDto.builder()
                .id(c.getId())
                .content(c.getContent())
                .ownerId(c.getOwner().getId())
                .ownerNickname(c.getOwner().getNickname())
                .createdAt(c.getCreatedAt())
                .build();
    }

    @Transactional
    public void deleteComment(User owner, Long commentId) {
        Comment comment = commentRepository
                .findByIdAndOwner(commentId, owner)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 없거나 권한이 없습니다. id=" + commentId));
        commentRepository.delete(comment);
    }
}
