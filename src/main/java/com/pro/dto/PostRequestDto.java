package com.pro.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostRequestDto {

    private Long postNo;
    private Long memberNo;      // 회원 번호
    private String title;       // 게시글 제목
    private String content;     // 게시글 내용
    private LocalDateTime registerDate;
    private Integer rating; // 별점 추가
    private String nickname;

}
