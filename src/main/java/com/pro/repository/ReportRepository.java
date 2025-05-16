package com.pro.repository;

import com.pro.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    // 한 사용자가 같은 게시글을 신고한 적 있는지 확인
    Optional<Report> findByPostNoAndReportedId(Long postNo, Long reporterMember);
}
