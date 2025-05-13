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

    @PostMapping("/{postNo}/report")
    public ResponseEntity<String> reportPost(@PathVariable Long postNo, @RequestBody ReportRequestDto dto) {
        dto.setPostNo(postNo); // 직접 DTO에 세팅
        reportService.reportPost(dto);
        return ResponseEntity.ok("게시글이 신고되었습니다.");
    }



}
