package com.pro.controller;

import com.pro.dto.CommentRequestDto;
import com.pro.dto.CommentResponseDto;
import com.pro.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community/posts/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto dto) {
        return ResponseEntity.ok(commentService.createComment(dto));
    }

    @GetMapping("/{postNo}")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postNo) {
        return ResponseEntity.ok(commentService.getCommentsByPostNo(postNo));
    }

    @PostMapping("/{commentNo}/report")
    public ResponseEntity<Void> reportComment(@PathVariable Long commentNo) {
        commentService.reportComment(commentNo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentNo}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentNo,
                                                @RequestParam(required = false) Long member) {
        Long testMember = 1003L;  // devMode에서 사용할 테스트 계정
        commentService.deleteComment(commentNo, member != null ? member : testMember);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}
