package com.pro.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class QnaRequestDto {
    private Long memberNo;
    private String questionContents;
}
