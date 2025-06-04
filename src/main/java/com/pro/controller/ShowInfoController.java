package com.pro.controller;

import com.pro.dto.ShowInfoDTO;
import com.pro.service.ShowInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/showinfo")
@CrossOrigin(origins = "*") // 개발 환경에서 CORS 허용
public class ShowInfoController {

    // 🔹 간단한 응답 전용 DTO 정의 (별도 파일 생성 없이 내부 record 사용)
    public static record CastDTO(Long id, String name, String role, String image) {}
    public static record ReviewPreviewDTO(Long id, String title, String preview, Long postId) {}
    public static record DetailResponse(String description, List<String> styleImgs) {}

    private final ShowInfoService showInfoService;

    // [1] 전체 공연 목록 조회
    // 예: GET /api/showinfo
    @GetMapping
    public ResponseEntity<List<ShowInfoDTO>> getAllShowInfos() {
        return ResponseEntity.ok(showInfoService.getAllShowInfos());
    }

    // [2] 공연 단건 조회 (공연 상세 또는 예매 시 사용)
    // 예: GET /api/showinfo/5
    @GetMapping("/{id}")
    public ResponseEntity<ShowInfoDTO> getShowInfo(@PathVariable Integer id) {
        return ResponseEntity.ok(showInfoService.getShowInfo(id));
    }

    // [3] 공연 정렬 조회 (이름, 날짜, 카테고리 등 정렬 기준 + 페이지네이션)
    // 예: GET /api/showinfo/sort?sortBy=name&direction=asc&page=0&size=20&excludeEnded=true
    @GetMapping("/sort")
    public ResponseEntity<Page<ShowInfoDTO>> getSortedShowInfoList(
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "false") boolean excludeEnded) {
        return ResponseEntity.ok(
                showInfoService.getSortedShowInfoList(sortBy, direction, page, size, excludeEnded));
    }

    // [4] 공연 검색 (키워드 기반 검색 + 페이지네이션)
    // 예: GET /api/showinfo/search?keyword=위키드&page=0&size=20
    @GetMapping("/search")
    public ResponseEntity<Page<ShowInfoDTO>> searchShowInfos(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(showInfoService.searchShowInfos(keyword, page, size));
    }

    // [5] 공연 카테고리 필터링
    // 예: GET /api/showinfo/filter?category=콘서트&page=0&size=20
    @GetMapping("/filter")
    public ResponseEntity<Page<ShowInfoDTO>> filterByCategory(
            @RequestParam String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(showInfoService.filterByCategory(category, page, size));
    }

    // [6] 공연 상세 설명 + 스타일 이미지 리스트 반환
    // 예: GET /api/showinfo/5/detail
    @GetMapping("/{id}/detail")
    public ResponseEntity<DetailResponse> detail(@PathVariable Integer id) {
        ShowInfoDTO dto = showInfoService.getShowInfo(id);
        List<String> imgs = Stream.of(
                        dto.getStyUrl1(),
                        dto.getStyUrl2(),
                        dto.getStyUrl3(),
                        dto.getStyUrl4())
                .filter(u -> u != null && !u.isBlank())
                .toList();
        return ResponseEntity.ok(new DetailResponse(dto.getExplain(), imgs));
    }

    // [7] 공연 출연 배우 목록 조회
    // 예: GET /api/showinfo/5/casts
    @GetMapping("/{id}/casts")
    public ResponseEntity<List<CastDTO>> casts(@PathVariable Integer id) {
        return ResponseEntity.ok(showInfoService.getCasts(id));
    }

    // [8] 공연 후기 미리보기 리스트 조회 (상위 N개만)
    // 예: GET /api/showinfo/5/reviews?size=5
    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewPreviewDTO>> reviews(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(showInfoService.getReviews(id, size));
    }
}
