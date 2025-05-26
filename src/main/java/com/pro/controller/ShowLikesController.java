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
     * GET /api/likes/check?showNo=...&userId=...
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> isLiked(
            @RequestParam Long showNo,
            @RequestParam Long userId
    ) {
        boolean liked = showLikesService.isLiked(showNo, userId);
        return ResponseEntity.ok(liked);
    }

    /**
     * 공연 찜 추가
     * POST /api/likes?showNo=...&userId=...
     */
    @PostMapping
    public ResponseEntity<Void> addLike(
            @RequestParam Long showNo,
            @RequestParam Long userId
    ) {
        showLikesService.addLike(showNo, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 공연 찜 삭제
     * DELETE /api/likes?showNo=...&userId=...
     */
    @DeleteMapping
    public ResponseEntity<Void> removeLike(
            @RequestParam Long showNo,
            @RequestParam Long userId
    ) {
        showLikesService.removeLike(showNo, userId);
        return ResponseEntity.ok().build();
    }
}
