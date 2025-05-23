package com.pro.service;

import com.pro.dto.ReservationDTO;
import com.pro.entity.Member;
import com.pro.entity.Reservation;
import com.pro.entity.Show;
import com.pro.repository.MemberRepository;
import com.pro.repository.ReservationRepository;
import com.pro.repository.SeatRepository;
import com.pro.repository.ShowRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createReservation(ReservationDTO dto) {
        if (dto.getSeatIdList().size() > 5) {
            throw new IllegalArgumentException("최대 5개의 좌석만 선택 가능합니다.");
        }

        Show show = showRepository.findById(dto.getShowId().intValue())
                .orElseThrow(() -> new IllegalArgumentException("해당 공연이 존재하지 않습니다."));

        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 존재하지 않습니다."));

        // ✅ 예약 날짜가 없으면 현재시간(LocalDateTime.now())로 대체!
        LocalDateTime reservationDate = dto.getReservationDate();
        if (reservationDate == null) {
            reservationDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        }

        for (Long seatId : dto.getSeatIdList()) {
            if (reservationRepository.existsBySeatId(seatId)) {
                throw new IllegalArgumentException("이미 예약된 좌석이 포함되어 있습니다. seatId=" + seatId);
            }

            ShowSeat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new IllegalArgumentException("좌석 정보가 존재하지 않습니다. seatId=" + seatId));

            seat.setSeatReserved(1);
            seatRepository.save(seat);

            Reservation reservation = Reservation.builder()
                    .show(show)
                    .member(member)
                    .seatId(seatId)
                    .reservationDate(reservationDate) // ✅ null 아님
                    .status(ReservationStatus.TEMP)
                    .build();

            reservationRepository.save(reservation);
        }
    }
}
