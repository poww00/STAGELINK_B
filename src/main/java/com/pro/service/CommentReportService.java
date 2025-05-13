package com.pro.service;

import com.pro.dto.CommentReportRequestDto;
import com.pro.entity.CommentReport;
import com.pro.repository.CommentReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentReportService {

    private final CommentReportRepository commentReportRepository;

    /**
     * 댓글 신고 등록 처리
     * @param dto 신고 요청 데이터
     */
    public void reportComment(CommentReportRequestDto dto) {
        CommentReport report = new CommentReport();
        report.setCommentNo(dto.getCommentNo());
        report.setReportedMember(dto.getReportedMember());
        report.setReporterMember(dto.getReporterMember());
        report.setReportReason(dto.getReportReason());
        report.setReportDate(LocalDateTime.now());

        commentReportRepository.save(report);
    }
}
