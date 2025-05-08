package com.pro.yeji.qna.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QnaResponseDto {
    private Long postNo;
    private String title;
    private String content;
    private LocalDateTime registerDate;
}
