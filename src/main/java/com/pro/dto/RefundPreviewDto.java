package com.pro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class RefundPreviewDto {
    private Long reservationNo;
    private String showTitle;
    private String poster;
    private String venue;
    private LocalDateTime showStartTime;
    private LocalDateTime reservationDate;
    private String seatClass;
    private String seatNumber;
    private int refundAmount;
    private int fee;
}

