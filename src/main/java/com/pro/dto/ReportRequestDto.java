package com.pro.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReportRequestDto {

    private Long reportNo;
    private Long postNo;             // 신고 대상 게시글 번호
    private Long suspectId;     // 피신고자 회원 번호
    private Long reportedId;     // 신고자 회원 번호
    private String reportReason;     // 신고 사유
    private LocalDateTime reportDate;
}
