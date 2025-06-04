package com.pro.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TBL_SHOWSEAT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowSeat {

    @Id
    @Column(name = "SEAT_ID")
    private Long seatId; //좌석 고유 번호

    @Column(name = "SEAT_CLASS")
    private String seatClass; // 등급 (R, S, A, VIP)

    @Column(name = "SEAT_NUMBER")
    private int seatNumber; //좌석 번호

    @Column(name = "SEAT_RESERVED")
    private int seatReserved; // 0: 미예약, 1: 예약됨

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHOW_NO")
    private Show show; // 공연 FK
}
