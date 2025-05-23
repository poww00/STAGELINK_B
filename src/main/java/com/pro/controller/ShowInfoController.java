package com.pro.controller;

import com.pro.dto.ShowInfoDTO;
import com.pro.service.ShowInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/showinfo")
public class ShowInfoController {

    private final ShowInfoService showInfoService;

    // 공연 기본 정보 전체 조회
    @GetMapping
    public ResponseEntity<List<ShowInfoDTO>> getAllShowInfos() {
        return ResponseEntity.ok(showInfoService.getAllShowInfos());
    }

    // 공연 기본 정보 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ShowInfoDTO> getShowInfo(@PathVariable Integer id) {
        return ResponseEntity.ok(showInfoService.getShowInfo(id));
    }

    // 공연 기본 정보 정렬 + 페이징 조회 + 종료공연 제외
    @GetMapping("/sort")
    public ResponseEntity<Page<ShowInfoDTO>> getSortedShowInfoList(
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "false") boolean excludeEnded) {

        Page<ShowInfoDTO> sortedList = showInfoService.getSortedShowInfoList(sortBy, direction, page, size, excludeEnded);
        return ResponseEntity.ok(sortedList);
    }

    // 공연 제목 검색 (페이징 포함)
    @GetMapping("/search")
    public ResponseEntity<Page<ShowInfoDTO>> searchShowInfos(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<ShowInfoDTO> result = showInfoService.searchShowInfos(keyword, page, size);
        return ResponseEntity.ok(result);
    }

    // 카테고리 필터링
    @GetMapping("/filter")
    public ResponseEntity<Page<ShowInfoDTO>> filterByCategory(
            @RequestParam String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<ShowInfoDTO> filtered = showInfoService.filterByCategory(category, page, size);
        return ResponseEntity.ok(filtered);
    }
/*
    // 배우명 검색 추가
    @GetMapping("/search/actor")
    public ResponseEntity<Page<ShowInfoDTO>> searchByActor(
            @RequestParam String actorName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<ShowInfoDTO> result = showInfoService.searchByActor(actorName, page, size);
        return ResponseEntity.ok(result);
    }

    // 공연장명 검색 추가
    @GetMapping("/search/location")
    public ResponseEntity<Page<ShowInfoDTO>> searchByLocation(
            @RequestParam String locationName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<ShowInfoDTO> result = showInfoService.searchByLocation(locationName, page, size);
        return ResponseEntity.ok(result);
    }

 */
}
