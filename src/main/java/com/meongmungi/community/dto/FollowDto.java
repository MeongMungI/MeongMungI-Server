package com.meongmungi.community.dto;

import com.meongmungi.community.domain.Follow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class FollowDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long followerId;
        private Long followingId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Long followerId;
        private Long followingId;
        private LocalDateTime createdAt;

        public static Response fromEntity(Follow follow) {
            return Response.builder()
                    .id(follow.getId())
                    .followerId(follow.getFollowerId())
                    .followingId(follow.getFollowingId())
                    .createdAt(follow.getCreatedAt())
                    .build();
        }
    }
}