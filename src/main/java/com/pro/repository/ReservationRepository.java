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

    /* ---------------- 좌석 중복 체크 ---------------- */
    boolean existsByShow_ShowNoAndSeatClassAndSeatIdAndStatusIn(
            int showNo, String seatClass, Long seatId, List<ReservationStatus> statusList);

    /* =================================================
       종합 TOP 20  (sh.SHOW_INFO = ShowInfo PK)
       ================================================= */
    @Query(value = """
        SELECT sh.SHOW_INFO  AS showInfoId,
               COUNT(*)      AS cnt
        FROM   TBL_RESERVATION r
        JOIN   TBL_SHOW       sh ON r.SHOW_NO = sh.SHOW_NO
        GROUP  BY sh.SHOW_INFO
        ORDER  BY cnt DESC
        LIMIT  20
    """, nativeQuery = true)
    List<Object[]> findTop20Shows();

    /* =================================================
       성별 TOP 20
       ================================================= */
    @Query(value = """
        SELECT sh.SHOW_INFO  AS showInfoId,
               COUNT(*)      AS cnt
        FROM   TBL_RESERVATION r
        JOIN   TBL_MEMBER     m  ON r.MEMBER  = m.MEMBER
        JOIN   TBL_SHOW       sh ON r.SHOW_NO = sh.SHOW_NO
        WHERE  m.GENDER = :gender
        GROUP  BY sh.SHOW_INFO
        ORDER  BY cnt DESC
        LIMIT  20
    """, nativeQuery = true)
    List<Object[]> findTop20ShowsByGender(@Param("gender") String gender);

    /* =================================================
       연령대(출생연도 범위) TOP 20
       ================================================= */
    @Query(value = """
        SELECT sh.SHOW_INFO  AS showInfoId,
               COUNT(*)      AS cnt
        FROM   TBL_RESERVATION r
        JOIN   TBL_MEMBER     m  ON r.MEMBER  = m.MEMBER
        JOIN   TBL_SHOW       sh ON r.SHOW_NO = sh.SHOW_NO
        WHERE  EXTRACT(YEAR FROM m.BIRTHDAY) BETWEEN :startYear AND :endYear
        GROUP  BY sh.SHOW_INFO
        ORDER  BY cnt DESC
        LIMIT  20
    """, nativeQuery = true)
    List<Object[]> findTop20ShowsByAgeRange(@Param("startYear") int startYear,
                                            @Param("endYear")   int endYear);
}
