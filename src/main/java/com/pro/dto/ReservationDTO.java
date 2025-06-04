package com.pro.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReservationDTO {

    /* 공통 */
    private Long userId;
    private Long showId;

    /* 좌석 선택(임시) */
    private List<String> seatLabelList;

    /* 확정·취소(다건) */
    private List<Long> reservationNoList;   // ✔️ 응답·요청 모두 이 이름 사용

    /* 단건 상세 조회 */
    private Long reservationNo;

    /* 결제 검증 */
    private Integer totalAmount;
    private String  impUid;

    /* 응답용 */
    private LocalDateTime reservationDate;
    private String        status;
}
