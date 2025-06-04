package com.pro.repository;

import com.pro.dto.RemainingSeatDTO;
import com.pro.entity.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<ShowSeat, Long> {

    /* ───────── 전체 좌석/가용 좌석 조회 ───────── */
    List<ShowSeat> findByShow_ShowNo(int showNo);
    List<ShowSeat> findByShow_ShowNoAndSeatReserved(int showNo, int seatReserved);

    /* ───────── 단일 좌석 조회 ───────── */
    @Query("""
           SELECT s
           FROM ShowSeat s
           WHERE s.show.showNo = :showNo
             AND s.seatClass   = :seatClass
             AND s.seatNumber  = :seatNumber
           """)
    Optional<ShowSeat> findSeat(@Param("showNo") int showNo,
                                @Param("seatClass") String seatClass,
                                @Param("seatNumber") int seatNumber);

    /* ───────── 등급별 잔여 좌석 수(집계) ───────── */
    @Query("""
           SELECT new com.pro.dto.RemainingSeatDTO(
                    SUM(CASE WHEN s.seatClass = 'VIP' AND s.seatReserved = 0 THEN 1 ELSE 0 END),
                    SUM(CASE WHEN s.seatClass = 'R'   AND s.seatReserved = 0 THEN 1 ELSE 0 END),
                    SUM(CASE WHEN s.seatClass = 'A'   AND s.seatReserved = 0 THEN 1 ELSE 0 END),
                    SUM(CASE WHEN s.seatClass = 'S'   AND s.seatReserved = 0 THEN 1 ELSE 0 END)
                  )
           FROM ShowSeat s
           WHERE s.show.showNo = :showNo
           """)
    RemainingSeatDTO findRemainingByShow(@Param("showNo") long showNo);
}
