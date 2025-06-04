package com.meongmungi.repository;


import com.meongmungi.entity.Post;
import com.meongmungi.entity.PostLike;
import com.meongmungi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    // 특정 게시글의 좋아요 개수 조회
    Long countByPost(Post post);

    // 특정 유저가 특정 게시글에 좋아요를 눌렀는지 여부 조회
    Optional<PostLike> findByPostAndOwner(Post post, User owner);
}
