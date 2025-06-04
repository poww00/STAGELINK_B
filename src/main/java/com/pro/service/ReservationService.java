package com.pro.service;

import com.pro.dto.ReservationDTO;
import com.pro.dto.ReservationDetailDTO;
import com.pro.entity.*;
import com.pro.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * ReservationService
 * 공연 좌석 예약 처리 서비스 클래스
 * - 임시 예약 → 확정/취소
 * - 예약 상세 조회
 */
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SeatRepository        seatRepository;
    private final ShowRepository        showRepository;
    private final MemberRepository      memberRepository;

    // [1] 좌석 선택 → 임시 예약 생성
    @Transactional
    public List<Long> createTempReservation(ReservationDTO dto) {
        if (dto.getSeatLabelList().size() > 5) {
            throw new IllegalArgumentException("최대 5개의 좌석만 선택 가능합니다.");
        }

        Member member = memberRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));
        Show show = showRepository.findById(dto.getShowId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회차가 없습니다."));

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        List<Long> reservationNos = new ArrayList<>();

        for (String label : dto.getSeatLabelList()) {
            String[] sp = label.split("-");
            String seatClass = sp[0];                   // 예: "VIP"
            int seatNumber = Integer.parseInt(sp[1]);   // 예: 12

            // 좌석 엔티티 조회
            ShowSeat seat = seatRepository
                    .findSeat(show.getShowNo().intValue(), seatClass, seatNumber)
                    .orElseThrow(() -> new IllegalArgumentException("좌석 정보가 없습니다."));

            Long seatNumberLong = Long.valueOf(seat.getSeatNumber());

            // 예약 중복 체크 (이미 예약된 좌석 포함 여부)
            if (seat.getSeatReserved() == 1 ||
                    reservationRepository.existsByShow_ShowNoAndSeatClassAndSeatIdAndStatusIn(
                            show.getShowNo().intValue(), seatClass, seatNumberLong,
                            List.of(ReservationStatus.TEMP, ReservationStatus.CONFIRMED))) {
                throw new IllegalArgumentException("이미 예약된 좌석이 포함돼 있습니다: " + label);
            }

            // 좌석 임시 선점
            seat.setSeatReserved(1);
            seatRepository.save(seat);

            // 예약 정보 저장
            Reservation r = Reservation.builder()
                    .show(show)
                    .member(member)
                    .seatClass(seatClass)
                    .seatId(seatNumberLong)
                    .reservationDate(now)
                    .status(ReservationStatus.TEMP)
                    .build();

            reservationNos.add(reservationRepository.save(r).getReservationNo());
        }

        return reservationNos;
    }

    //[2] 결제 성공 → 예약 확정 처리
    @Transactional
    public void confirmReservation(List<Long> reservationNos) {
        List<Reservation> list = reservationRepository.findAllById(reservationNos);

        for (Reservation r : list) {
            if (r.getStatus() != ReservationStatus.TEMP) {
                throw new IllegalStateException("이미 확정/취소된 예약이 포함돼 있습니다.");
            }
            r.setStatus(ReservationStatus.CONFIRMED);
            r.setReservationDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        }

        reservationRepository.saveAll(list);
    }

    // [3] 예약 취소 처리
    @Transactional
    public void cancelReservation(List<Long> reservationNos) {
        List<Reservation> list = reservationRepository.findAllById(reservationNos);

        for (Reservation r : list) {
            r.setStatus(ReservationStatus.CANCELED);

            // 좌석 예약 해제
            ShowSeat seat = seatRepository
                    .findSeat(r.getShow().getShowNo().intValue(),
                            r.getSeatClass(),
                            r.getSeatId().intValue())
                    .orElse(null);

            if (seat != null) {
                seat.setSeatReserved(0);
                seatRepository.save(seat);
            }
        }

        reservationRepository.saveAll(list);
    }

    // [4] 단건 예약 상세 조회 (결제 완료 후 요약 확인용)
    @Transactional(readOnly = true)
    public ReservationDetailDTO getReservationDetail(Long reservationNo) {
        Reservation r = reservationRepository.findById(reservationNo)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보가 없습니다."));

        Show show = r.getShow();
        ShowInfo showInfo = show.getShowInfo();

        // 좌석 등급에 따른 가격 추출
        int seatPrice = switch (r.getSeatClass()) {
            case "VIP" -> show.getSeatVipPrice();
            case "R"   -> show.getSeatRPrice();
            case "S"   -> show.getSeatSPrice();
            case "A"   -> show.getSeatAPrice();
            default    -> 0;
        };

        return ReservationDetailDTO.builder()
                .reservationNo(r.getReservationNo())
                .showTitle(showInfo.getName())
                .date(show.getShowStartTime().toLocalDate().toString())
                .time(show.getShowStartTime().toLocalTime().toString())
                .seatLabel(r.getSeatClass() + "-" + r.getSeatId())
                .totalAmount(seatPrice)
                .build();
    }
}
