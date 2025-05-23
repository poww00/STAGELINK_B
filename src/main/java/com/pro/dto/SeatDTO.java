package com.pro.dto;

import com.pro.entity.ShowSeat;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatDTO {

    private Long seatId;
    private String seatClass;
    private int seatNumber;
    private boolean reserved; // 프론트 편의상 boolean으로 제공

    // 엔티티 → DTO 변환 메서드
    public static SeatDTO fromEntity(ShowSeat seat) {
        return SeatDTO.builder()
                .seatId(seat.getSeatId())
                .seatClass(seat.getSeatClass())
                .seatNumber(seat.getSeatNumber())
                .reserved(seat.getSeatReserved() == 1)
                .build();
    }
}
