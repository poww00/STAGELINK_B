package com.pro.repository;

import com.pro.entity.Reservation;
import com.pro.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * ReservationRepository
 * 예매 정보 관련 JPA 레포지토리
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 좌석 중복 예약 여부 확인 (예: TEMP 또는 CONFIRMED 상태 포함)
    boolean existsByShow_ShowNoAndSeatClassAndSeatIdAndStatusIn(
            int showNo, String seatClass, Long seatId, List<ReservationStatus> statusList);

    // 전체 인기 공연 (판매량 상위 20개)
    @Query(value = "SELECT r.show_no, COUNT(*) AS cnt " +
            "FROM tbl_reservation r " +
            "GROUP BY r.show_no ORDER BY cnt DESC", nativeQuery = true)
    List<Object[]> findTop20Shows();

    // 성별 기준 인기 공연
    @Query(value = "SELECT r.show_no, COUNT(*) AS cnt " +
            "FROM tbl_reservation r " +
            "JOIN tbl_member m ON r.member = m.member " +
            "WHERE m.gender = :gender " +
            "GROUP BY r.show_no ORDER BY cnt DESC", nativeQuery = true)
    List<Object[]> findTop20ShowsByGender(@Param("gender") String gender);

    // 연령대 기준 인기 공연
    @Query(value = "SELECT r.show_no, COUNT(*) AS cnt " +
            "FROM tbl_reservation r " +
            "JOIN tbl_member m ON r.member = m.member " +
            "WHERE EXTRACT(YEAR FROM m.birthday) BETWEEN :startYear AND :endYear " +
            "GROUP BY r.show_no ORDER BY cnt DESC", nativeQuery = true)
    List<Object[]> findTop20ShowsByAgeRange(@Param("startYear") int startYear,
                                            @Param("endYear") int endYear);
}
