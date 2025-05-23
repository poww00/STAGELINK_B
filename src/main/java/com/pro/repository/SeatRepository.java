package com.pro.repository;

import com.pro.entity.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<ShowSeat, Long> {

    // ✅ 필드명 정확히 맞춰야 함: showNo
    List<ShowSeat> findByShow_ShowNoAndSeatReserved(int showNo, int seatReserved);
}
