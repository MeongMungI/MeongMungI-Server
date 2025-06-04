package com.meongmungi.dto;

import lombok.*;

import java.time.LocalDateTime;

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
}