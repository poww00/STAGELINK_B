package com.pro.controller;

import com.pro.dto.QnaRatingDto;
import com.pro.dto.QnaRequestDto;
import com.pro.dto.QnaResponseDto;
import com.pro.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community/qna")
@RequiredArgsConstructor
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
    public ResponseEntity<QnaResponseDto> createQna(@RequestBody QnaRequestDto dto) {
        return ResponseEntity.ok(qnaService.createQna(dto));
    }

    @PostMapping("/{id}/rating")
    public ResponseEntity<QnaResponseDto> updateRating(@PathVariable Long id, @RequestBody QnaRatingDto dto) {
        return ResponseEntity.ok(qnaService.updateRating(id, dto));
    }
}
