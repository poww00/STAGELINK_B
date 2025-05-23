package com.pro.repository;

import com.pro.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsBySeatId(Long seatId);

    // 전체 공연 판매량 순
    @Query(value = "SELECT r.show_no, COUNT(*) AS cnt " +
            "FROM tbl_reservation r " +
            "GROUP BY r.show_no ORDER BY cnt DESC", nativeQuery = true)
    List<Object[]> findTop20Shows();

    // 성별 기준 인기 공연 (Native Query + String 파라미터)
    @Query(value = "SELECT r.show_no, COUNT(*) AS cnt " +
            "FROM tbl_reservation r " +
            "JOIN tbl_member m ON r.member = m.member " +
            "WHERE m.gender = :gender " +
            "GROUP BY r.show_no ORDER BY cnt DESC", nativeQuery = true)
    List<Object[]> findTop20ShowsByGender(@Param("gender") String gender); // ✅ 이름 통일

    // 연령대 기준 인기 공연 (Native Query)
    @Query(value = "SELECT r.show_no, COUNT(*) AS cnt " +
            "FROM tbl_reservation r " +
            "JOIN tbl_member m ON r.member = m.member " +
            "WHERE EXTRACT(YEAR FROM m.birthday) BETWEEN :startYear AND :endYear " +
            "GROUP BY r.show_no ORDER BY cnt DESC", nativeQuery = true)
    List<Object[]> findTop20ShowsByAgeRange(@Param("startYear") int startYear,
                                            @Param("endYear") int endYear); // ✅ 이름 통일
}
