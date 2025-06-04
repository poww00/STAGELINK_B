package com.pro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequestDto {
    private Long reservationNo; // 어떤 예매를 환불할 건지
}
