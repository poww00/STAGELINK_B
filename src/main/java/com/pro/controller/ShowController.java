package com.pro.controller;

import com.pro.dto.ShowDTO;
import com.pro.service.ShowService;
import lombok.RequiredArgsConstructor;
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

    // 특정 상태의 공연 조회
    @GetMapping("/shows/titles")
    public ResponseEntity<List<ShowDTO>> getShowsByStates(@RequestParam List<Integer> states) {
        return ResponseEntity.ok(showService.getShowsByStates(states));
    }
}
