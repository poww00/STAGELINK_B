package com.pro.controller;

import com.pro.dto.ReportRequestDto;
import com.pro.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community/posts")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/{id}/report")
    public ResponseEntity<String> reportPost(
            @PathVariable("id") Long postNo,
            @RequestBody ReportRequestDto dto
    ) {
        reportService.reportPost(dto);
        return ResponseEntity.ok("게시글 신고가 접수되었습니다.");
    }


}
