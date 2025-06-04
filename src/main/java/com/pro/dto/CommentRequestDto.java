package com.pro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {

    private Long postNo;               // 게시글 번호
    private String commentContent;     // 댓글 내용
    private Integer commentRating;     // 평점 (nullable)
}
