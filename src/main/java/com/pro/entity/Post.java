package com.pro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postNo; // 게시글 번호 (PK)

    private Long memberNo; // 작성자 회원 번호

    private String title; // 게시글 제목

    @Column(columnDefinition = "TEXT")
    private String content; // 게시글 내용

    private LocalDateTime registerDate = LocalDateTime.now(); // 작성일시

    @Column(name = "rating")
    private Integer rating; // 1~5점 정수형 별점

    @Column(name = "nickname", length = 20)
    private String nickname;    // 닉네임

}
