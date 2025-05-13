package com.pro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_no")
    private Long commentNo;  // 댓글 번호

    @Column(name = "post_no", nullable = false)
    private Long postNo;     // 게시글 번호

    @Column(name = "member", nullable = false)
    private Long memberNo;     // 회원 번호

    @Column(name = "comment_register_date")
    private LocalDateTime commentRegisterDate = LocalDateTime.now();  // 작성일

    @Column(name = "comment_content", length = 500)
    private String content;  // 댓글 내용

    @Column(name = "comment_rating")
    private Integer commentRating;  // 평점 (1~5점)

    @Column(name = "comment_report_count")
    private Integer commentReportCount = 0;  // 신고 횟수

    @Column(name = "nickname", length = 20)
    private String nickname;                // 닉네임

}
