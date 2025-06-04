package com.pro.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationDetailDTO {

    private Long    reservationNo; //예매번호
    private String  showTitle; //공연 제목
    private String  date;        // 예매한 날짜 (형식: yyyy-MM-dd)
    private String  time;        // 예매한 회차 시간 (형식: HH:mm)
    private String  seatLabel;   // 좌석 라벨 (예: "VIP-24")
    private Integer totalAmount; // 총 결제 금액 (할인 포함 최종 금액)
}
