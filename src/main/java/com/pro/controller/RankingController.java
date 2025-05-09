package com.pro.controller;

import com.pro.dto.ShowInfoDTO;
import com.pro.entity.Gender;
import com.pro.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main/ranking")
public class RankingController {

    private final RankingService rankingService;

    // 전체 판매량 TOP 20
    @GetMapping("/top20")
    public ResponseEntity<List<ShowInfoDTO>> getTop20Shows() {
        return ResponseEntity.ok(rankingService.getTop20Shows());
    }

    // 성별 인기 공연 TOP 20 (gender: MALE/FEMALE)
    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<ShowInfoDTO>> getTop20ShowsByGender(@PathVariable Gender gender) {
        return ResponseEntity.ok(rankingService.getTop20ShowsByGender(gender));
    }

    // 연령대별 인기 공연 TOP 20
    @GetMapping("/age")
    public ResponseEntity<Map<String, List<ShowInfoDTO>>> getTop20ShowsByAgeGroups() {
        return ResponseEntity.ok(rankingService.getTop20ShowsByAgeGroups());
    }
}
