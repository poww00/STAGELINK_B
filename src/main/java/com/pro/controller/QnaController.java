package com.pro.controller;

import com.pro.dto.QnaRatingDto;
import com.pro.dto.QnaRequestDto;
import com.pro.dto.QnaResponseDto;
import com.pro.security.user.CustomUserDetails;
import com.pro.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community/qna")
public class QnaController {

    private final QnaService qnaService;

    @GetMapping
    public ResponseEntity<List<QnaResponseDto>> getQnaList() {
        return ResponseEntity.ok(qnaService.getAllQna());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QnaResponseDto> getQnaById(@PathVariable Long id) {
        return ResponseEntity.ok(qnaService.getQnaById(id));
    }

    @PostMapping
    public ResponseEntity<QnaResponseDto> createQna(@RequestBody QnaRequestDto dto,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long member = userDetails.getId();
        return ResponseEntity.ok(qnaService.createQna(dto, member));
    }

    @PostMapping("/{id}/rating")
    public ResponseEntity<QnaResponseDto> updateRating(@PathVariable Long id,
                                                       @RequestBody QnaRatingDto dto,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long member = userDetails.getId();
        dto.setMember(member);
        return ResponseEntity.ok(qnaService.updateRating(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQna(@PathVariable Long id,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long member = userDetails.getId();
        qnaService.deleteQna(id, member);
        return ResponseEntity.ok("QnA가 삭제되었습니다.");
    }
}
