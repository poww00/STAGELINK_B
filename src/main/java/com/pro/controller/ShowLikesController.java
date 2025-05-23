package com.pro.controller;

import com.pro.service.ShowLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 공연 찜하기(Like) 컨트롤러
 * - 찜 추가(POST) / 삭제(DELETE) / 확인(GET)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class ShowLikesController {

    private final ShowLikesService showLikesService;

    /**
     * 찜 여부 확인
     * GET /api/likes/check?showNo=...&memberId=...
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> isLiked(
            @RequestParam Long showNo,
            @RequestParam Long memberId
    ) {
        boolean liked = showLikesService.isLiked(showNo, memberId);
        return ResponseEntity.ok(liked);
    }

    /**
     * 공연 찜 추가
     * POST /api/likes?showNo=...&memberId=...
     */
    @PostMapping
    public ResponseEntity<Void> addLike(
            @RequestParam Long showNo,
            @RequestParam Long memberId
    ) {
        showLikesService.addLike(showNo, memberId);
        return ResponseEntity.ok().build();
    }

    /**
     * 공연 찜 삭제
     * DELETE /api/likes?showNo=...&memberId=...
     */
    @DeleteMapping
    public ResponseEntity<Void> removeLike(
            @RequestParam Long showNo,
            @RequestParam Long memberId
    ) {
        showLikesService.removeLike(showNo, memberId);
        return ResponseEntity.ok().build();
    }
}
