package com.meongmungi.dto;

import com.meongmungi.entity.CategoryType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private CategoryType category;

    // 작성자 정보
    private Long ownerId;
    private String ownerNickname;

    // 댓글 개수
    private Long commentCount;

    // 좋아요 개수
    private Long likeCount;

    // 이미지 URL 리스트
    private List<String> imageUrls;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}