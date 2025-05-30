package com.pro.dto;

import com.pro.entity.Qna;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QnaResponseDto {

    private Long questionNo;
    private Long member;                // DTO 필드는 member로 유지
    private String questionContents;
    private String answerContents;
    private LocalDateTime createDate;
    private Integer qnaRating;

    public QnaResponseDto(Qna qna) {
        this.questionNo = qna.getQuestionNo();
        this.member = qna.getMemberNo(); // DB 필드명 memberNo → DTO member 로 변환
        this.questionContents = qna.getQuestionContents();
        this.answerContents = qna.getAnswerContents();
        this.createDate = qna.getCreateDate();
        this.qnaRating = qna.getQnaRating();
    }
}
