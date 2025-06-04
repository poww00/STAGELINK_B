package com.pro.service.Impl;

import com.pro.dto.RefundPreviewDto;
import com.pro.dto.RefundPreviewProjection;
import com.pro.dto.RefundResponseDto;
import com.pro.entity.*;
import com.pro.repository.MyReservationRepository;
import com.pro.repository.RefundRepository;
import com.pro.repository.ReservationRepository;
import com.pro.repository.SeatRepository;
import com.pro.service.RefundCalculator;
import com.pro.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;
    private final RefundRepository refundRepository;
    private final MyReservationRepository myReservationRepository;

    @Override
    @Transactional
    public RefundResponseDto processRefund(Long reservationId, Long userId) {
        // [1] 예매 정보 조회
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예매 내역이 존재하지 않습니다."));

        // [2] 유저 검증
        if (!reservation.getMember().getId().equals(userId)) {
            throw new IllegalArgumentException("해당 예매에 대한 환불 권한이 없습니다.");
        }

        // [3] 예매 상태 확인
        if (reservation.getStatus() == ReservationStatus.CANCELED) {
            throw new IllegalArgumentException("이미 취소된 예매입니다.");
        }

        // [4] 결제 금액 및 환불 금액 계산
        int totalAmount = calculateAmountBySeatClass(reservation.getSeatClass(), reservation.getShow());

        RefundCalculator.RefundResult result = RefundCalculator.calculate(
                reservation.getReservationDate(),
                reservation.getShow().getShowStartTime(),
                totalAmount
        );

        // [5] 좌석 정보 조회
        ShowSeat seat = seatRepository.findSeat(
                reservation.getShow().getShowNo().intValue(),
                reservation.getSeatClass(),
                reservation.getSeatId().intValue()
        ).orElseThrow(() -> new IllegalArgumentException("좌석 정보를 찾을 수 없습니다."));

        // [6] 환불 엔티티 생성 및 저장
        Refund refund = Refund.builder()
                .member(reservation.getMember())
                .reservation(reservation)
                .seatNo(seat)
                .seatClass(reservation.getSeatClass())
                .refundDate(LocalDate.now())
                .build();
        refundRepository.save(refund);

        // [7] 예매 상태 변경 + 좌석 예약 상태 복구
        reservation.setStatus(ReservationStatus.CANCELED);
        seat.setSeatReserved(0);
        seatRepository.save(seat);

        // [8] 결과 DTO 반환
        return new RefundResponseDto(
                refund.getRefundNo(),
                refund.getMember().getId(),
                refund.getReservation().getReservationNo(),
                refund.getSeatNo().getSeatId(),
                refund.getSeatClass(),
                refund.getRefundDate(),
                result.getRefundAmount(),
                result.getFee()
        );
    }




    // 환불 안내
    @Transactional(readOnly = true)
    @Override
   public RefundPreviewDto getRefundPreview(Long reservationId, Long userId) {
        // 예매 정보 조회
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예매 내역이 존재하지 않습니다."));

        // 본인 예매인지 확인
        if (!reservation.getMember().getId().equals(userId)) {
            throw new IllegalArgumentException("해당 예매건에 대한 환불 안내 권한이 없습니다.");
        }

        // 환불 미리보기 Projection 조회
        RefundPreviewProjection projection = myReservationRepository.findRefundPreviewByReservationId(reservationId);

        if (projection == null) {
            throw new IllegalArgumentException("환불 안내 정보를 찾을 수 없습니다.");
        }

        // 환불 금액 계산
        RefundCalculator.RefundResult result = RefundCalculator.calculate(
                projection.getReservationDate(),
                projection.getShowStartTime(),
                projection.getRefundAmount()
        );

        return RefundPreviewDto.builder()
                .reservationNo(projection.getReservationNo())
                .showTitle(projection.getShowTitle())
                .poster(projection.getPoster())
                .venue(projection.getVenue())
                .showStartTime(projection.getShowStartTime())
                .reservationDate(projection.getReservationDate())
                .seatClass(projection.getSeatClass())
                .seatNumber(projection.getSeatNumber())
                .refundAmount(result.getRefundAmount())
                .fee(result.getFee())
                .build();
    }



    // 좌석 등급에 따라 해당 좌석 가격을 계산해주는 메서드
    private int calculateAmountBySeatClass(String seatClass, Show show) {
        return switch (seatClass) {
            case "R" -> show.getSeatRPrice();
            case "S" -> show.getSeatSPrice();
            case "A" -> show.getSeatAPrice();
            case "VIP" -> show.getSeatVipPrice();
            default -> throw new IllegalArgumentException("유효하지 않은 좌석 등급입니다.");
        };
    }
}
