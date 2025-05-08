package com.pro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnaRequestDto {
    private String title;
    private String content;
    private Long member;
}
