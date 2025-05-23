package com.pro.controller;

import com.pro.dto.QnaRatingDto;
import com.pro.dto.QnaRequestDto;
import com.pro.dto.QnaResponseDto;
import com.pro.service.QnaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community/qna")
public class QnaController {

    private final QnaService qnaService;
    private final boolean devMode = false; //  테스트용 모드

    @GetMapping
    public ResponseEntity<List<QnaResponseDto>> getQnaList() {
        return ResponseEntity.ok(qnaService.getAllQna());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QnaResponseDto> getQnaById(@PathVariable Long id) {
        return ResponseEntity.ok(qnaService.getQnaById(id));
    }

    @PostMapping
    public ResponseEntity<QnaResponseDto> createQna(@RequestBody QnaRequestDto dto, HttpServletRequest request) {
        Long memberId;
        Object memberNoAttr = request.getAttribute("memberNo");

        if (devMode || memberNoAttr == null) {
            memberId = 1003L; // 테스트용
        } else {
            memberId = Long.valueOf((int) memberNoAttr);
        }

        return ResponseEntity.ok(qnaService.createQna(dto, memberId));
    }

    @PostMapping("/{id}/rating")
    public ResponseEntity<QnaResponseDto> updateRating(@PathVariable Long id, @RequestBody QnaRatingDto dto) {
        return ResponseEntity.ok(qnaService.updateRating(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQna(@PathVariable Long id, HttpServletRequest request) {
        Long memberId;
        Object memberNoAttr = request.getAttribute("memberNo");

        if (devMode || memberNoAttr == null) {
            memberId = 1003L; // 테스트용
        } else {
            memberId = Long.valueOf((int) memberNoAttr);
        }

        qnaService.deleteQna(id, memberId);
        return ResponseEntity.ok("QnA가 삭제되었습니다.");
    }

}

