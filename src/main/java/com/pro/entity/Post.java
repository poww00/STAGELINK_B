package com.pro.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_POST")
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_NO")
    private int postNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_NO")  // 정확하게 FK 컬럼명에 맞춰야 함
    private Member member;

    @Column(name = "NICKNAME", nullable = false, length = 20)
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_SHOW_NO", nullable = false)
    private Show show;

    @Column(name = "POST_TITLE", nullable = false, length = 50)
    private String postTitle;

    @Column(name = "POST_CONTENT", nullable = false, length = 1000)
    private String postContent;

    @Column(name = "POST_RATING")
    private int postRating;

    @Column(name = "POST_REGISTER_DATE")
    private LocalDateTime postRegisterDate;

    @Column(name = "POST_REPORT_COUNT")
    private int postReportCount;
}
