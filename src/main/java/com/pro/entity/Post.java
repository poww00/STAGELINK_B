package com.pro.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_post")
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postNo;

    private int type; // 0: 일반, 1: 공지사항, 2: QnA

    private String title;

    private String content;

    private Long member;

    private LocalDateTime registerDate;

    private Integer rating;
}
