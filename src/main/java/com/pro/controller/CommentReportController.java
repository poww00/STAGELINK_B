package com.pro.controller;

import com.pro.dto.CommentReportRequestDto;
import com.pro.service.CommentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community/comments")
@RequiredArgsConstructor
public class CommentReportController {

    private final CommentReportService commentReportService;

    /**
     * 댓글 신고 API
     * @param dto 신고 요청 데이터
     * @return 처리 결과 메시지
     */
    @PostMapping("/{commentNo}/report")
    public ResponseEntity<String> reportComment(@RequestBody CommentReportRequestDto dto) {
        commentReportService.reportComment(dto);
        return ResponseEntity.ok("댓글 신고가 정상적으로 접수되었습니다.");
    }
}
