package com.pro.controller;

import com.pro.dto.ShowDTO;
import com.pro.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ShowController {

    private final ShowService showService;

    // 공연 전체 조회
    @GetMapping("/shows")
    public ResponseEntity<List<ShowDTO>> getAllShows() {
        return ResponseEntity.ok(showService.getAllShows());
    }

    // 공연 상세 조회
    @GetMapping("/shows/{id}")
    public ResponseEntity<ShowDTO> getShow(@PathVariable Integer id) {
        return ResponseEntity.ok(showService.getShow(id));
    }

    // 특정 상태의 공연 조회 (페이지네이션)
    @GetMapping("/show/titles")
    public ResponseEntity<Page<ShowDTO>> getShowsByStates(
            @RequestParam List<Integer> states,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(showService.getShowsByStates(states, page, size));
    }

    // 공연의 모든 회차(ShowInfo 기준) 조회
    @GetMapping("/shows/byShowInfo/{showInfoId}")
    public ResponseEntity<List<ShowDTO>> getShowsByShowInfo(@PathVariable Integer showInfoId) {
        return ResponseEntity.ok(showService.getShowsByShowInfo(showInfoId));
    }
}
