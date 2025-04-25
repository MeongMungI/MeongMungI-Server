package com.meongmungi.community.service;

import com.meongmungi.community.domain.Like;
import com.meongmungi.community.domain.Post;
import com.meongmungi.community.dto.LikeDto;
import com.meongmungi.community.repository.LikeRepository;
import com.meongmungi.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @Transactional
    public LikeDto.Response addLike(Long postId, LikeDto.Request request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        Optional<Like> existingLike = likeRepository.findByPostIdAndUserId(postId, request.getUserId());

        if (existingLike.isPresent()) {
            return LikeDto.Response.fromEntity(existingLike.get());
        }

        Like like = Like.builder()
                .post(post)
                .userId(request.getUserId())
                .build();

        Like savedLike = likeRepository.save(like);
        return LikeDto.Response.fromEntity(savedLike);
    }

    @Transactional
    public void removeLike(Long postId, Long userId) {
        if (!likeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new IllegalArgumentException("해당 좋아요가 존재하지 않습니다.");
        }

        likeRepository.deleteByPostIdAndUserId(postId, userId);
    }

    public boolean checkIfUserLikedPost(Long postId, Long userId) {
        return likeRepository.existsByPostIdAndUserId(postId, userId);
    }

    public int getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}