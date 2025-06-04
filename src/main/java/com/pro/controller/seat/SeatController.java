package com.pro.controller.seat;

import com.pro.dto.RemainingSeatDTO;
import com.pro.dto.SeatDTO;
import com.pro.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    // [1] 전체 좌석 조회 (예약 가능/완료 여부 관계없이 전체)
    // 예: GET /api/seats/all?showId=1
    @GetMapping("/all")
    public ResponseEntity<List<SeatDTO>> getAllSeats(@RequestParam Long showId) {
        List<SeatDTO> seats = seatService.getAllSeats(showId);
        return ResponseEntity.ok(seats);
    }

    // [2] 예약 가능한 좌석만 조회 (사용 안 할 수도 있음)
    // 예: GET /api/seats/available?showId=1
    @GetMapping("/available")
    public ResponseEntity<List<SeatDTO>> getAvailableSeats(@RequestParam Long showId) {
        List<SeatDTO> seats = seatService.getAvailableSeats(showId);
        return ResponseEntity.ok(seats);
    }

    // [3] 좌석 자동 배치 생성 (관리자/테스트용)
    // 예: GET /api/seats/generate/{showId}
    @GetMapping("/generate/{showId}")
    public ResponseEntity<List<SeatDTO>> generateSeats(@PathVariable Long showId) {
        List<SeatDTO> seatList = seatService.generateSeats(showId);
        return ResponseEntity.ok(seatList);
    }

    // [4] 남은 좌석 수 조회 (예매창 우측 정보 패널용)
    // 예: GET /api/seats/remaining?showId=1
    @GetMapping("/remaining")
    public ResponseEntity<RemainingSeatDTO> getRemaining(@RequestParam long showId) {
        return ResponseEntity.ok(seatService.getRemaining(showId));
    }
}
