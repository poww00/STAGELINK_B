package com.pro.controller;

import com.pro.dto.ShowInfoDTO;
import com.pro.entity.Gender;
import com.pro.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main/ranking")
public class RankingController {

    private final RankingService rankingService;

    /** 전체 판매량 TOP 20 */
    @GetMapping("/top20")
    public ResponseEntity<List<ShowInfoDTO>> getTop20Shows() {
        return ResponseEntity.ok(rankingService.getTop20Shows());
    }

    /** 성별 인기 TOP 20 (MALE / FEMALE) */
    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<ShowInfoDTO>> getTop20ShowsByGender(
            @PathVariable Gender gender) {
        return ResponseEntity.ok(rankingService.getTop20ShowsByGender(gender));
    }

    /** 연령대 인기 TOP 20 (10대·20대·…·60대이상) */
    @GetMapping("/age/{ageGroup}")
    public ResponseEntity<List<ShowInfoDTO>> getTop20ShowsByAge(
            @PathVariable String ageGroup) {
        return ResponseEntity.ok(rankingService.getTop20ShowsByAge(ageGroup));
    }
}
