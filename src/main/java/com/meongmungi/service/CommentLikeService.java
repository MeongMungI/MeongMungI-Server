package com.meongmungi.service;

import com.meongmungi.dto.LikeResponseDto;
import com.meongmungi.entity.Comment;
import com.meongmungi.entity.CommentLike;
import com.meongmungi.entity.User;
import com.meongmungi.repository.CommentLikeRepository;
import com.meongmungi.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepository likeRepo;
    private final CommentRepository commentRepo;

    @Transactional
    public LikeResponseDto likeComment(User owner, Long commentId) {
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다. id=" + commentId));
        if (likeRepo.findByCommentAndOwner(c, owner).isPresent())
            throw new IllegalArgumentException("이미 좋아요했습니다.");
        likeRepo.save(CommentLike.builder().comment(c).owner(owner).build());
        return buildDto(c, true);
    }

    @Transactional
    public LikeResponseDto unlikeComment(User owner, Long commentId) {
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다. id=" + commentId));
        CommentLike existing = likeRepo.findByCommentAndOwner(c, owner)
                .orElseThrow(() -> new IllegalArgumentException("좋아요를 누르지 않았습니다."));
        likeRepo.delete(existing);
        return buildDto(c, false);
    }

    @Transactional(readOnly=true)
    public LikeResponseDto getLikeStatus(User owner, Long commentId) {
        Comment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다. id=" + commentId));
        boolean liked = likeRepo.findByCommentAndOwner(c, owner).isPresent();
        return buildDto(c, liked);
    }

    private LikeResponseDto buildDto(Comment c, boolean liked) {
        Long cnt = likeRepo.countByComment(c);
        return LikeResponseDto.builder()
                .likeCount(cnt)
                .likedByCurrentUser(liked)
                .build();
    }
}