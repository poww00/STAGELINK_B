package com.pro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_SHOW")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Show {

    //공연 고유 번호 (PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHOW_NO")
    private Long showNo;

    //공연 정보 번호 (FK - TBL_SHOWINFO의 SHOW_INFO 참조)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHOW_INFO", nullable = false)
    private ShowInfo showInfo;

    //공연 시작 일시 (형식: yyyy-MM-dd HH:mm:ss)
    @Column(name = "SHOW_START_TIME", nullable = false)
    private LocalDateTime showStartTime;

    //공연 종료 일시 (형식: yyyy-MM-dd HH:mm:ss)
    @Column(name = "SHOW_END_TIME", nullable = false)
    private LocalDateTime showEndTime;

    //R석 가격
    @Column(name = "SEAT_R_PRICE", nullable = false)
    private Integer seatRPrice;

    //A석 가격
    @Column(name = "SEAT_A_PRICE", nullable = false)
    private Integer seatAPrice;

    //S석 가격
    @Column(name = "SEAT_S_PRICE", nullable = false)
    private Integer seatSPrice;

    //VIP석 가격
    @Column(name = "SEAT_VIP_PRICE", nullable = false)
    private Integer seatVipPrice;

    //R석 총 좌석 수
    @Column(name = "SEAT_R_COUNT", nullable = false)
    private Integer seatRCount;

    //A석 총 좌석 수
    @Column(name = "SEAT_A_COUNT", nullable = false)
    private Integer seatACount;

    //S석 총 좌석 수
    @Column(name = "SEAT_S_COUNT", nullable = false)
    private Integer seatSCount;


    //VIP석 총 좌석 수
    @Column(name = "SEAT_VIP_COUNT", nullable = false)
    private Integer seatVipCount;

    // 공연 상태  0: 판매예정, 1: 판매중, 2: 예약종료(품절)
    @Column(name = "SHOW_STATE", nullable = false, length = 1)
    private Integer showState;
}
