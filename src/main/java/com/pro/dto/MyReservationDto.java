package com.pro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyReservationDto {
    private String reservationDate;     // 예매일 (2025.04.30)
    private String showTitle;           // 공연 제목
    private String showPeriod;          // 공연 기간 (2025.06.11 ~ 2025.06.13)
    private String venue;               // 공연 장소
    private Long reservationNumber;   // 예매번호
    private String watchDateTime;       // 관람일시 (2025.06.11 19:00)
    private int ticketCount;            // 매수 (1매)
    private String status;              // 상태 (예매완료 / 취소완료 등)
    private String poster;

}
