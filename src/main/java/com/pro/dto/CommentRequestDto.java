package com.pro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {

    private Long postNo;         // 게시글 번호
    private Long memberNo;       // 회원 번호 ← 여기가 중요
    private String content;      // 댓글 내용 ← 여기도!
    private Integer commentRating;  // 평점 (선택)
}
