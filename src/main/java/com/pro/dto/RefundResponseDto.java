package com.pro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class RefundResponseDto {
    private Long refundNo;
    private Long memberId;
    private Long reservationNo;
    private Long seatNo;
    private String seatClass;
    private LocalDate refundDate;
    private int refundAmount;
    private int fee;
}
