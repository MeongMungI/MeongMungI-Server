package com.meongmungi.repository;

import com.meongmungi.entity.Post;
import com.meongmungi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.xml.stream.events.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 게시글의 모든 댓글 조회
    List<Comment> findAllByPost(Post post);

    // 특정 유저가 작성한 댓글 조회
    List<Comment> findAllByOwner(User owner);

    Optional<Comment> findByIdAndOwner(Long commentId, User owner);
}
