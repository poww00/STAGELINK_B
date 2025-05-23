package com.pro.service;

import com.pro.dto.SeatDTO;
import com.pro.entity.Show;
import com.pro.entity.ShowSeat;
import com.pro.repository.SeatRepository;
import com.pro.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;

    // 예약 가능한 좌석 리스트 조회
    public List<SeatDTO> getAvailableSeats(Long showId, LocalDate date, Integer round) {
        // 💡 임시로 date/round는 무시하고 SHOW_NO 기준으로만 필터링
        List<ShowSeat> seatList = seatRepository.findByShow_ShowNoAndSeatReserved(showId.intValue(), 0);

        return seatList.stream()
                .map(SeatDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 좌석 자동 배치 생성
    public List<SeatDTO> generateSeats(Integer showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공연이 존재하지 않습니다."));

        List<SeatDTO> seats = new ArrayList<>();
        int id = 1;

        seats.addAll(createSeats("VIP", show.getSeatVipCount(), id));
        id += show.getSeatVipCount();

        seats.addAll(createSeats("R", show.getSeatRCount(), id));
        id += show.getSeatRCount();

        seats.addAll(createSeats("S", show.getSeatSCount(), id));

        return seats;
    }

    private List<SeatDTO> createSeats(String grade, int count, int startId) {
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
