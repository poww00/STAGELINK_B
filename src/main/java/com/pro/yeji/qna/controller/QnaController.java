package com.pro.yeji.qna.controller;

import com.pro.yeji.qna.dto.QnaRatingDto;
import com.pro.yeji.qna.dto.QnaRequestDto;
import com.pro.yeji.qna.dto.QnaResponseDto;
import com.pro.yeji.qna.service.QnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/qna")
public class QnaController {

    private final QnaService qnaService;

    @Autowired
    public QnaController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    // 전체 QnA 조회
    @GetMapping
    public List<QnaResponseDto> getQnaList() {
        return qnaService.getAllQna();
    }

    // QnA 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<QnaResponseDto> getQnaById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(qnaService.getQnaById(id));
    }

    // QnA 등록
    @PostMapping
    public ResponseEntity<QnaResponseDto> createQna(@RequestBody QnaRequestDto qna) {
        return ResponseEntity.ok(qnaService.createQna(qna));
    }

    // QnA 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQna(@PathVariable Long id) {
        qnaService.deleteQna(id);
        return ResponseEntity.ok("QnA 삭제 완료");
    }

    @PutMapping("/{id}/rating")
    public ResponseEntity<String> updateRating(@PathVariable Long id, @RequestBody QnaRatingDto dto) {
        qnaService.updateRating(id, dto.getRating());
        return ResponseEntity.ok("별점이 등록되었습니다.");
    }

}
