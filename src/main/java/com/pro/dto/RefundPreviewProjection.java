package com.pro.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface RefundPreviewProjection {
    Long getReservationNo();
    String getShowTitle();
    String getPosterUrl();
    String getVenue();
    LocalDateTime getShowStartTime();
    LocalDateTime getReservationDate();
    String getSeatClass();
    String getSeatNumber();
    Integer getRefundAmount(); // 보통 총 결제금액 (계산 기반)
}

