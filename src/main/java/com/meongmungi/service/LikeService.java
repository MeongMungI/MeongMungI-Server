package com.meongmungi.service;

import com.meongmungi.dto.LikeResponseDto;
import com.meongmungi.entity.Post;
import com.meongmungi.entity.PostLike;
import com.meongmungi.entity.User;
import com.meongmungi.repository.PostLikeRepository;
import com.meongmungi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    /**
     * 게시글 좋아요 추가
     */
    @Transactional
    public LikeResponseDto likePost(User owner, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));

        // 이미 좋아요를 눌렀는지 확인
        boolean alreadyLiked = postLikeRepository.findByPostAndOwner(post, owner).isPresent();
        if (alreadyLiked) {
            throw new IllegalArgumentException("이미 좋아요를 눌렀습니다.");
        }

        PostLike like = PostLike.builder()
                .owner(owner)
                .post(post)
                .build();
        postLikeRepository.save(like);

        Long likeCount = postLikeRepository.countByPost(post);
        return LikeResponseDto.builder()
                .likeCount(likeCount)
                .likedByCurrentUser(true)
                .build();
    }

    /**
     * 게시글 좋아요 취소
     */
    @Transactional
    public LikeResponseDto unlikePost(User owner, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));

        PostLike existing = postLikeRepository.findByPostAndOwner(post, owner)
                .orElseThrow(() -> new IllegalArgumentException("좋아요를 누른 적이 없습니다."));
        postLikeRepository.delete(existing);

        Long likeCount = postLikeRepository.countByPost(post);
        return LikeResponseDto.builder()
                .likeCount(likeCount)
                .likedByCurrentUser(false)
                .build();
    }

    /**
     * 특정 게시글의 좋아요 상태 조회
     */
    @Transactional(readOnly = true)
    public LikeResponseDto getLikeStatus(User owner, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));

        Long likeCount = postLikeRepository.countByPost(post);
        boolean likedByUser = postLikeRepository.findByPostAndOwner(post, owner).isPresent();

        return LikeResponseDto.builder()
                .likeCount(likeCount)
                .likedByCurrentUser(likedByUser)
                .build();
    }
}