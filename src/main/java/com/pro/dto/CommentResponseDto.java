package com.pro.dto;

import com.pro.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDto {

    private Long commentNo;
    private Long postNo;
    private Long member;
    private LocalDateTime commentRegisterDate;
    private String commentContent;
    private Integer commentRating;
    private Integer commentReportCount;
    private String nickname;


    public CommentResponseDto(Comment comment) {
        this.commentNo = comment.getCommentNo();
        this.postNo = comment.getPostNo();
        this.member = comment.getMemberNo();
        this.commentRegisterDate = comment.getCommentRegisterDate();
        this.commentContent = comment.getContent();
        this.commentRating = comment.getCommentRating();
        this.commentReportCount = comment.getCommentReportCount();
        this.nickname = comment.getNickname();
    }
}
