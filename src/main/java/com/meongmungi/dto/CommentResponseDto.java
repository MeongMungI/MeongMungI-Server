package com.meongmungi.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {

    private Long id;
    private String content;

    private Long ownerId;
    private String ownerNickname;

    private LocalDateTime createdAt;

    // 대댓글 포함
    @Builder.Default
    private List<CommentResponseDto> replies = new ArrayList<>();
}