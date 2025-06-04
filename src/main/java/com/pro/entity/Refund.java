package com.pro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "TBL_REFUND")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REFUND_NO")
    private Long refundNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER", nullable = false)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESERVATION_NO", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEAT_NO", nullable = false)
    private ShowSeat seatNo;

    @Column(name = "REFUND_DATE", nullable = false)
    private LocalDate refundDate;

    @Column(name = "SEAT_CLASS", nullable = false)
    private String seatClass; // 좌석 등급 (R, S, A, VIP)
}
