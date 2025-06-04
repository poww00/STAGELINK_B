package com.pro.repository;

import com.pro.dto.RefundPreviewProjection;
import com.pro.dto.ReservationDetailProjection;
import com.pro.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MyReservationRepository extends JpaRepository<Reservation, Long> {
    // 탈퇴 가능 여부 확인(예매건이 있는지 확인)
    boolean existsByMember_Id(Long userId);

    // 예매 건수 (관람일 지난 공연 & 예매 취소 제외)
    @Query(value = """
        SELECT COUNT(*)
        FROM tbl_reservation r
        JOIN tbl_show s ON r.show_no = s.show_no
        WHERE r.member = :memberId
          AND r.status IN ('TEMP', 'CONFIRMED')
          AND s.show_start_time > NOW()
    """, nativeQuery = true)
    int countValidReservations(@Param("memberId") Long memberId);

    // 전체 예매내역 조회
    @Query(value = """
        SELECT 
            DATE_FORMAT(r.reservation_date, '%Y.%m.%d') AS reservationDate,
            si.show_name AS showTitle,
            CONCAT(DATE_FORMAT(s.show_start_time, '%Y.%m.%d'), ' ~ ', DATE_FORMAT(s.show_end_time, '%Y.%m.%d')) AS showPeriod,
            l.location_name AS venue,
            r.reservation_no AS reservationNumber,
            DATE_FORMAT(s.show_start_time, '%Y.%m.%d %H:%i') AS watchDateTime,
            COUNT(*) AS ticketCount,
            r.status AS status,
            si.show_poster As poster
        FROM tbl_reservation r
        JOIN tbl_show s ON r.show_no = s.show_no
        JOIN tbl_showinfo si ON s.show_info = si.show_info
        JOIN tbl_showlocation l ON si.show_location = l.showlocation_id
        WHERE r.member = :memberId
        GROUP BY r.reservation_no
        """, nativeQuery = true)
    List<Object[]> findMyReservations(@Param("memberId") Long memberId);

    // 예매 상세 조회
    @Query(value = """
        SELECT 
            r.reservation_no AS reservationId,
            DATE_FORMAT(r.reservation_date, '%Y.%m.%d') AS reservationDate,
            si.show_name AS showTitle,
            DATE_FORMAT(s.show_start_time, '%Y.%m.%d %H:%i') AS showDateTime,
            l.location_name AS venue,
            ss.seat_class AS seatClass,
            ss.seat_number AS seatNumber,
            r.status AS status,
            m.name AS buyerName,
            si.show_poster AS poster,
            DATE_FORMAT(DATE_SUB(s.show_start_time, INTERVAL 1 DAY), '%Y.%m.%d %23:%59') AS cancelAvailableUntil,
            CASE 
                WHEN ss.seat_class = 'R' THEN s.seat_r_price
                WHEN ss.seat_class = 'S' THEN s.seat_s_price
                WHEN ss.seat_class = 'A' THEN s.seat_a_price
                WHEN ss.seat_class = 'VIP' THEN s.seat_vip_price
                ELSE 0
            END AS totalAmount
        FROM tbl_reservation r
        JOIN tbl_show s ON r.show_no = s.show_no
        JOIN tbl_showinfo si ON s.show_info = si.show_info
        JOIN tbl_showlocation l ON si.show_location = l.showlocation_id
        JOIN tbl_member m ON r.member = m.member
        JOIN tbl_showseat ss ON r.seat_id = ss.seat_id
        WHERE r.reservation_no = :reservationId
    """, nativeQuery = true)
    Optional<ReservationDetailProjection> findReservationDetail(@Param("reservationId") Long reservationId);

    // Reservation 엔티티 직접 조회
    @Query("SELECT r FROM Reservation r WHERE r.id = :reservationId")
    Optional<Reservation> findReservationEntityById(@Param("reservationId") Long reservationId);

    Optional<Reservation> findReservationByReservationNo(Long memberId);


    // 환불 안내 (환불 전)
    @Query(value = """
    SELECT 
        r.reservation_no AS reservationNo,
        si.show_name AS showTitle,
        si.show_poster AS poster,
        l.location_name AS venue,
        s.show_start_time AS showStartTime,
        r.reservation_date AS reservationDate,
        ss.seat_class AS seatClass,
        ss.seat_number AS seatNumber,
        CASE 
            WHEN ss.seat_class = 'R' THEN s.seat_r_price
            WHEN ss.seat_class = 'S' THEN s.seat_s_price
            WHEN ss.seat_class = 'A' THEN s.seat_a_price
            WHEN ss.seat_class = 'VIP' THEN s.seat_vip_price
            ELSE 0
        END AS refundAmount
    FROM tbl_reservation r
    JOIN tbl_show s ON r.show_no = s.show_no
    JOIN tbl_showinfo si ON s.show_info = si.show_info
    JOIN tbl_showseat ss ON r.seat_id = ss.seat_id
    JOIN tbl_showlocation l ON si.show_location = l.showlocation_id
    WHERE r.reservation_no = :reservationId
    """, nativeQuery = true)
    RefundPreviewProjection findRefundPreviewByReservationId(@Param("reservationId") Long reservationId);

}
