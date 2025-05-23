package com.pro.service;

import com.pro.dto.ReportRequestDto;
import com.pro.entity.Post;
import com.pro.entity.Report;
import com.pro.repository.PostRepository;
import com.pro.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;

    public void reportPost(ReportRequestDto dto) {
        // 신고 저장
        Report report = new Report();
        report.setPostNo(dto.getPostNo());                    // 신고 대상 게시글 번호
        report.setSuspectId(dto.getSuspectId());    // 피신고자
        report.setReportedId(dto.getReportedId());    // 신고자
        report.setReportReason(dto.getReportReason());        // 신고 사유
        report.setReportDate(LocalDateTime.now());      // 신고 날짜

        reportRepository.save(report);

        // 신고 카운트 증가
        Post post = postRepository.findById(dto.getPostNo().intValue())
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
        post.setPostReportCount(post.getPostReportCount() + 1); // 신고 수 1 증가
        postRepository.save(post);
    }
}
