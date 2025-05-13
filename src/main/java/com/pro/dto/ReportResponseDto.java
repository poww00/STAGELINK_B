package com.pro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ReportResponseDto {

    private Long reportNo;
    private Long postNo;
    private Long reporterNo;
    private String reason;

    public ReportResponseDto(Long reportNo, Long postNo, Long reporterNo, String reason) {
        this.reportNo = reportNo;
        this.postNo = postNo;
        this.reporterNo = reporterNo;
        this.reason = reason;
    }
}
