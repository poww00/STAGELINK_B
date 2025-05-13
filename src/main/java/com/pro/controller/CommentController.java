package com.pro.controller;

import com.pro.dto.CommentRequestDto;
import com.pro.dto.CommentResponseDto;
import com.pro.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community/posts/comments") //  기본 경로
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //  GET /api/community/posts/comments/1
    @GetMapping("/{postNo}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPost(@PathVariable Long postNo) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postNo));
    }

    //  POST /api/community/posts/comments
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto dto) {
        return ResponseEntity.ok(commentService.createComment(dto));
    }

    //  DELETE /api/community/posts/comments/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("댓글 삭제 완료");
    }
}
