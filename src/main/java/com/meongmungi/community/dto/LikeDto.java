package com.meongmungi.community.dto;

import com.meongmungi.community.domain.Like;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class LikeDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long userId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Long postId;
        private Long userId;
        private LocalDateTime createdAt;

        public static Response fromEntity(Like like) {
            return Response.builder()
                    .id(like.getId())
                    .postId(like.getPost().getId())
                    .userId(like.getUserId())
                    .createdAt(like.getCreatedAt())
                    .build();
        }
    }
}