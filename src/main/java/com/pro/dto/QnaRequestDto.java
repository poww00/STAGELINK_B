package com.pro.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class QnaRequestDto {
    private Long questionNo;
    private Long memberNo;
    private String questionContents;
    private String answerContents;
    private LocalDateTime createDate;
    private Integer qnaRating;
}
