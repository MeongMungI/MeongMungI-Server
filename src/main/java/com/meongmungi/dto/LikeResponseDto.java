package com.meongmungi.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeResponseDto {

    // 해당 게시글 좋아요 개수
    private Long likeCount;

    // 현재 유저가 해당 게시글에 좋아요를 눌렀는지 여부
    private Boolean likedByCurrentUser;
}