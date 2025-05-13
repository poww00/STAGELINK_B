package com.pro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_no")
    private Long reportNo;  // 신고 번호

    @Column(name = "post_no", nullable = false)
    private Long postNo;  // 게시글 번호 (또는 댓글 대상 글 번호)

    @Column(name = "reported_member", nullable = false)
    private Long reportedMember;  // 피신고자 회원 번호

    @Column(name = "reporter_member", nullable = false)
    private Long reporterMember;  // 신고자 회원 번호

    @Column(name = "report_date")
    private LocalDateTime reportDate = LocalDateTime.now();  // 신고일

    @Column(name = "report_reason")
    private String reportReason;  // 신고 사유
}
