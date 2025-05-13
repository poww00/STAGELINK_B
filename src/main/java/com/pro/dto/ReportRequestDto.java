package com.pro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequestDto {

    private Long postNo;             // 신고 대상 게시글 번호
    private Long reportedMember;     // 피신고자 회원 번호
    private Long reporterMember;     // 신고자 회원 번호
    private String reportReason;     // 신고 사유
}
