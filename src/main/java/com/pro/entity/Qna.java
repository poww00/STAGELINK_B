package com.pro.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_qnaswer") // 💡 현재 테이블명이 이거니까 그대로 유지
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Qna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_no") // 🔥 꼭 필요합니다
    private Long questionNo;

    @Column(name = "member_no", nullable = false)
    private Long memberNo;

    @Column(name = "question_contents", nullable = false)
    private String questionContents;

    @Column(name = "answer_contents")
    private String answerContents;

    @CreationTimestamp
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @Column(name = "rating")
    private Integer rating;
}
