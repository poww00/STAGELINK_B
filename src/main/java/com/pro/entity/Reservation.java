package com.pro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_RESERVATION")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    // 예매 번호 (PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVATION_NO")
    private Long reservationNo;

    // 회원 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER", nullable = false)
    private Member member;

    // 공연 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHOW_NO", nullable = false)
    private Show show;

    // 좌석 ID
    @Column(name = "SEAT_ID", nullable = false)
    private Long seatId;

    @Column(name = "SEAT_CLASS", nullable = false)
    private String seatClass;

    // 예매 날짜
    @Column(name = "RESERVATION_DATE", nullable = false)
    private LocalDateTime reservationDate;

    //좌석 상태
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private ReservationStatus status;

    // 임시 추가(깃 받을 시 수정할 것)
    // 좌석 등급
    @Column(name = "SEAT_CLASS", nullable = false)
    private String seatClass;

}
