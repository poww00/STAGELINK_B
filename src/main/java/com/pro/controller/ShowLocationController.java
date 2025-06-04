package com.pro.controller;

import com.pro.dto.ShowLocationDTO;
import com.pro.service.ShowLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 공연장 위치 정보 API 컨트롤러
 * - 공연장 주소 및 이름 조회 기능 제공
 */
@RestController
@RequestMapping("/api/showlocation")
@RequiredArgsConstructor
public class ShowLocationController {

    private final ShowLocationService showLocationService;

    // [1] 공연장 위치 정보 단건 조회
    // 예: GET /api/showlocation/1
    // 공연 상세 페이지에서 공연장 주소 출력 시 사용
    @GetMapping("/{id}")
    public ShowLocationDTO getLocation(@PathVariable Long id) {
        System.out.println("📍 공연장 주소 요청 들어옴: " + id);
        return showLocationService.getLocation(id);
    }
}

