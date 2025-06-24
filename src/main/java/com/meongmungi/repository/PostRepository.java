package com.meongmungi.repository;


import com.meongmungi.entity.CategoryType;
import com.meongmungi.entity.Post;
import com.meongmungi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 특정 사용자가 작성한 모든 게시글 조회
    List<Post> findAllByOwner(User owner);

    // 카테고리별 게시글 조회
    List<Post> findAllByCategory(CategoryType category);

    // ‘인기’ 카테고리는 좋아요 수 내림차순 조회
    @Query("SELECT p FROM Post p LEFT JOIN p.likes l GROUP BY p ORDER BY COUNT(l) DESC")
    List<Post> findAllOrderByLikesDesc();
}