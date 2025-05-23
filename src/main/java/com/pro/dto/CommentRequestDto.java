package com.pro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {

    private Long postNo;             // POST_NO (게시글 번호)
    private Long member;             // MEMBER (회원 번호)
    private String nickname;         // NICKNAME (닉네임)
    private String commentContent;   // COMMENT_CONTENT (댓글 내용)
    private Integer commentRating;   // COMMENT_RATING (평점)
}
