package com.pro.controller.seat;

import com.pro.dto.SeatDTO;
import com.pro.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    /**
     * ✅ 좌석 조회 API
     * GET /api/seats/available?showId=1&date=2025-05-01&round=1
     */
    @GetMapping("/available")
    public ResponseEntity<List<SeatDTO>> getAvailableSeats(
            @RequestParam Long showId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Integer round
    ) {
        List<SeatDTO> seats = seatService.getAvailableSeats(showId, date, round);
        return ResponseEntity.ok(seats);
    }

    /**
     * ✅ 좌석 자동 배치 생성 API
     * GET /api/seats/generate/{showId}
     */
    @GetMapping("/generate/{showId}")
    public ResponseEntity<List<SeatDTO>> generateSeats(@PathVariable Integer showId) {
        List<SeatDTO> seatList = seatService.generateSeats(showId);
        return ResponseEntity.ok(seatList);
    }
}