package com.pro.dto;

import com.pro.entity.Qna;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class QnaResponseDto {
    private Long questionNo;
    private Long memberNo;
    private String questionContents;
    private String answerContents;
    private LocalDateTime createDate;
    private Integer rating;

    public QnaResponseDto(Qna qna) {
        this.questionNo = qna.getQuestionNo();
        this.memberNo = qna.getMemberNo();
        this.questionContents = qna.getQuestionContents();
        this.answerContents = qna.getAnswerContents();
        this.createDate = qna.getCreateDate();
        this.rating = qna.getRating();
    }
}
