package com.pro.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 댓글 신고 요청에 사용되는 DTO 클래스
 */
@Getter
@Setter
public class CommentReportRequestDto {

    // 신고 대상 댓글 번호
    private Long commentNo;

    // 피의자 회원 번호 (댓글 작성자)
    private Long reportedMember;

    // 신고자 회원 번호
    private Long reporterMember;

    // 신고 사유
    private String reportReason;
}
