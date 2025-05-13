package com.pro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDto {

    private Long memberNo;      // 회원 번호
    private String title;       // 게시글 제목
    private String content;     // 게시글 내용
}
