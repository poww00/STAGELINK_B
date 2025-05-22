package com.pro.dto;

import com.pro.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDto {

    private Long commentNo;              // COMMENT_NO
    private Long postNo;                 // POST_NO
    private String nickname;             // NICKNAME
    private Long member;                 // MEMBER
    private LocalDateTime commentRegisterDate;   // COMMENT_REGISTER_DATE
    private String commentContent;       // COMMENT_CONTENT
    private Integer commentRating;       // COMMENT_RATING
    private Integer commentReportCount;  // COMMENT_REPORT_COUNT

    public CommentResponseDto(Comment comment) {
        this.commentNo = comment.getCommentNo();
        this.postNo = comment.getPostNo();
        this.nickname = comment.getNickname();
        this.member = comment.getMember(); // member로 수정
        this.commentRegisterDate = comment.getCommentRegisterDate();
        this.commentContent = comment.getCommentContent();
        this.commentRating = comment.getCommentRating();
        this.commentReportCount = comment.getCommentReportCount();
    }
}
