package com.pro.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_comment_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_report_no")
    private Long commentReportNo;  // 신고 번호

    @Column(name = "comment_no", nullable = false)
    private Long commentNo;        // 댓글 번호

    @Column(name = "reported_member", nullable = false)
    private Long reportedMember;   // 피의자 회원 번호

    @Column(name = "reporter_member", nullable = false)
    private Long reporterMember;   // 신고자 회원 번호

    @Column(name = "report_date")
    private LocalDateTime reportDate = LocalDateTime.now(); // 신고일

    @Column(name = "report_reason", nullable = false)
    private String reportReason;   // 신고 사유
}
