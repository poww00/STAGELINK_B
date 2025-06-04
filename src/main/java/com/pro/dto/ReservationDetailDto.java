package com.pro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// 최종적으로 클라이언트에게 넘겨주는 DTO (프론트에 전달할 객체)
@Getter
@Builder
@AllArgsConstructor
public class ReservationDetailDto {
    private Long reservationId;
    private String reservationDate;
    private String showTitle;
    private String showDateTime;
    private String venue;
    private String seatClass;
    private String seatNumber;
    private String status;
    private String buyerName;
    private String cancelAvailableUntil;
    private Integer totalAmount;
    private String poster;
}


