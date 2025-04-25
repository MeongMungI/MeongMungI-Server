package com.meongmungi.community.dto;

import com.meongmungi.community.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class PostDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String content;
        private String imageUrl;
        private Long userId;
        private String userName;
        private String userProfileImage;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String content;
        private String imageUrl;
        private Long userId;
        private String userName;
        private String userProfileImage;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private int likeCount;
        private int commentCount;
        private boolean isLiked;

        public static Response fromEntity(Post post, boolean isLiked) {
            return Response.builder()
                    .id(post.getId())
                    .content(post.getContent())
                    .imageUrl(post.getImageUrl())
                    .userId(post.getUserId())
                    .userName(post.getUserName())
                    .userProfileImage(post.getUserProfileImage())
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .likeCount(post.getLikeCount())
                    .commentCount(post.getCommentCount())
                    .isLiked(isLiked)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResponse {
        private Long id;
        private String content;
        private String imageUrl;
        private Long userId;
        private String userName;
        private String userProfileImage;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private int likeCount;
        private int commentCount;
        private boolean isLiked;
        private List<CommentDto.Response> comments;

        public static DetailResponse fromEntity(Post post, boolean isLiked, List<CommentDto.Response> comments) {
            return DetailResponse.builder()
                    .id(post.getId())
                    .content(post.getContent())
                    .imageUrl(post.getImageUrl())
                    .userId(post.getUserId())
                    .userName(post.getUserName())
                    .userProfileImage(post.getUserProfileImage())
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .likeCount(post.getLikeCount())
                    .commentCount(post.getCommentCount())
                    .isLiked(isLiked)
                    .comments(comments)
                    .build();
        }
    }
}