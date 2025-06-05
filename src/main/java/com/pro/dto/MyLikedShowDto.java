package com.pro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyLikedShowDto {

    private Long showNo;         // 공연 ID
    private String showName;     // 공연 제목
    private String poster;       // 공연 포스터 이미지
    private String period;       // 공연 기간
    private boolean available;   // 예매 가능 여부

    // 명시적 생성자
    public MyLikedShowDto(Long showNo, String showName, String poster, String period, boolean available) {
        this.showNo = showNo;
        this.showName = showName;
        this.poster = poster;
        this.period = period;
        this.available = available;
    }

    // 기본 생성자 (JPA 또는 Jackson 등에서 필요할 수 있음)
    public MyLikedShowDto() {}
}