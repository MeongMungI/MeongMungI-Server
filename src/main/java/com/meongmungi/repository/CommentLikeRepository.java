package com.meongmungi.repository;

import com.meongmungi.entity.Comment;
import com.meongmungi.entity.CommentLike;
import com.meongmungi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentAndOwner(Comment comment, User owner);
    Long countByComment(Comment comment);
}
