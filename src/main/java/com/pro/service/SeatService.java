package com.pro.service;

import com.pro.dto.RemainingSeatDTO;
import com.pro.dto.SeatDTO;
import com.pro.entity.Show;
import com.pro.entity.ShowSeat;
import com.pro.repository.SeatRepository;
import com.pro.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SeatService
 * 공연 좌석 관련 서비스 클래스
 * - 공연 회차별 좌석 조회
 * - 남은 좌석 정보 반환
 * - 좌석 자동 생성 (관리자/테스트 용도)
 */
@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;
    private final SeatRepository repo; // RemainingSeatDTO 계산용 (seatRepository와 동일 객체)

    //공연 회차 ID로 전체 좌석 리스트 반환 (예약 여부 무관)
    public List<SeatDTO> getAllSeats(Long showId) {
        List<ShowSeat> seatList = seatRepository.findByShow_ShowNo(showId.intValue());
        return seatList.stream()
                .map(SeatDTO::fromEntity)
                .collect(Collectors.toList());
    }

    //공연 회차 ID로 예약 가능한 좌석만 반환
    public List<SeatDTO> getAvailableSeats(Long showId) {
        List<ShowSeat> seatList = seatRepository.findByShow_ShowNoAndSeatReserved(showId.intValue(), 0);
        return seatList.stream()
                .map(SeatDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 공연 회차 ID로 남은 좌석 수 집계 정보 반환
    public RemainingSeatDTO getRemaining(long showId) {
        return repo.findRemainingByShow(showId);
    }

    // 공연 회차에 좌석 자동 생성
    public List<SeatDTO> generateSeats(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공연이 존재하지 않습니다."));

        List<SeatDTO> seats = new ArrayList<>();
        int id = 1;

        // 등급별 좌석 생성
        seats.addAll(createSeats("VIP", show.getSeatVipCount(), id, show));
        id += show.getSeatVipCount();
        seats.addAll(createSeats("R", show.getSeatRCount(), id, show));
        id += show.getSeatRCount();
        seats.addAll(createSeats("A", show.getSeatACount(), id, show));
        id += show.getSeatACount();
        seats.addAll(createSeats("S", show.getSeatSCount(), id, show));

        return seats;
    }

    // 특정 등급 좌석을 지정 수만큼 생성
    private List<SeatDTO> createSeats(String grade, int count, int startId, Show show) {
        List<SeatDTO> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(SeatDTO.builder()
                    .seatId((long) (startId + i))
                    .seatClass(grade)
                    .seatNumber(i + 1)
                    .reserved(false)
                    .build());
        }
        return result;
    }
}
