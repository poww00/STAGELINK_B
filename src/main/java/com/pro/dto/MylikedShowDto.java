package com.pro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MylikedShowDto {
    private Long showNo;        // 공연 ID
    private String showName;       // 공연 제목
    private String poster;   // 공연 포스터 이미지
    private String period;      // 공연 기간
    private boolean available;  // 예매 가능 여부
}
