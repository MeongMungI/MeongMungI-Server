package com.meongmungi.community.repository;

import com.meongmungi.community.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Post> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.userId IN (SELECT f.followingId FROM Follow f WHERE f.followerId = :userId) ORDER BY p.createdAt DESC")
    Page<Post> findFollowingPosts(@Param("userId") Long userId, Pageable pageable);
}