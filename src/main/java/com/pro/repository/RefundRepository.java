package com.pro.repository;

import com.pro.entity.Member;
import com.pro.entity.Refund;
import com.pro.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund, Long> {
    // 예매 번호로 환불 기록을 조회
    Optional<Refund> findByReservation(Reservation reservation);

    // 사용자(member) + 예매 번호 조합으로 조회
    //Optional<Refund> findByMemberAndReservation(Member member, Reservation reservationNo);
}
