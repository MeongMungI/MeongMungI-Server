package com.meongmungi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDto {

    @NotBlank(message = "댓글 내용은 필수 항목입니다.")
    private String content;

    // parentCommentId가 null이면 최상위 댓글 아니면 답글
    private Long parentCommentId;
}
