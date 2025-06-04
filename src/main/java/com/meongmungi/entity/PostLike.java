package com.meongmungi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_likes",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_post_user",
                columnNames = {"post_id", "user_id"}
        ))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostLike {

    //@Table 유니크 조건으로 같은 유저가 같은 게시글에 한 번만 좋아요을 누를 수 있게 강제

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ────────── 좋아요 누른 유저 ──────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    // ────────── 좋아요 대상 게시글 ──────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // 생성 시각
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}