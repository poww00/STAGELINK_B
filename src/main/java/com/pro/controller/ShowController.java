package com.pro.controller;

import com.pro.dto.ShowDTO;
import com.pro.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ShowDTO> getShow(@PathVariable Long id) {
        return ResponseEntity.ok(showService.getShow(id));
    }

    // 현재 예매 가능한 공연만 조회 (판매중 상태)
    @GetMapping("/shows/available")
    public ResponseEntity<List<ShowDTO>> getAvailableShows() {
        return ResponseEntity.ok(showService.getAvailableShows());
    }
}
