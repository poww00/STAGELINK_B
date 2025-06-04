package com.pro.dto;

import com.pro.entity.ShowSeat;
import lombok.*;

/**
 * SeatDTO
 * 공연 좌석 정보를 담는 DTO 클래스
 * 프론트엔드에 좌석 ID, 등급, 좌석 번호, 예약 여부 등을 전달하기 위해 사용됨
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatDTO {

    private Long seatId;       // 좌석 고유 ID (PK)

    private String seatClass;  // 좌석 등급 (예: VIP, R, S 등)

    private int seatNumber;    // 좌석 번호 (1, 2, 3, ...)

    private boolean reserved;  // 예약 여부 (true: 예약됨, false: 예약 가능)
    // 프론트엔드에서 직관적으로 처리할 수 있도록 boolean으로 변환

    /**
     * ShowSeat 엔티티 → SeatDTO 변환 메서드
     * @param seat ShowSeat 엔티티 객체
     * @return SeatDTO 객체
     */
    public static SeatDTO fromEntity(ShowSeat seat) {
        return SeatDTO.builder()
                .seatId(seat.getSeatId())
                .seatClass(seat.getSeatClass())
                .seatNumber(seat.getSeatNumber())
                .reserved(seat.getSeatReserved() == 1) // DB의 1 → true, 0 → false
                .build();
    }
}
