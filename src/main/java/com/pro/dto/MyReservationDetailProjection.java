package com.pro.dto;

public interface MyReservationDetailProjection {
    Long getReservationId(); // 예매번호
    String getReservationDate(); // 예매 일자
    String getShowTitle(); // 공연명
    String getShowDateTime(); // 관람 일시
    String getVenue(); // 공연장 이름
    String getSeatClass(); // 좌석 등급
    String getSeatNumber(); // 좌석 번호
    String getStatus(); // 예매 상태
    String getBuyerName(); // 예매자 이름
    String getCancelAvailableUntil(); // 취소 가능 일시
    Integer getTotalAmount(); // 총 결제 금액
    String getPoster();
}
