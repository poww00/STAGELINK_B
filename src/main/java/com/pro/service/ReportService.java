package com.pro.service;

import com.pro.dto.ReportRequestDto;
import com.pro.entity.Report;
import com.pro.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public void reportPost(ReportRequestDto dto) {
        Report report = new Report();
        report.setPostNo(dto.getPostNo());                    // 신고 대상 게시글 번호
        report.setSuspectId(dto.getSuspectId());    // 피신고자
        report.setReportedId(dto.getReportedId());    // 신고자
        report.setReportReason(dto.getReportReason());        // 신고 사유
        report.setReportDate(LocalDateTime.now());      // 신고 날짜

        reportRepository.save(report);
    }
}
