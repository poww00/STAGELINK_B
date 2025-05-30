package com.pro.controller;

import com.pro.dto.CommentRequestDto;
import com.pro.dto.CommentResponseDto;
import com.pro.security.user.CustomUserDetails;
import com.pro.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community/posts/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto dto,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long member = userDetails.getId();
        String nickname = userDetails.getNickname();  // CustomUserDetails에 이미 있음
        return ResponseEntity.ok(commentService.createComment(dto, member, nickname));
    }

    // 게시글별 댓글 목록 조회
    @GetMapping("/{postNo}")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postNo) {
        return ResponseEntity.ok(commentService.getCommentsByPostNo(postNo));
    }

    // 댓글 신고
    @PostMapping("/{commentNo}/report")
    public ResponseEntity<Void> reportComment(@PathVariable Long commentNo,
                                              @RequestParam("reason") String reason,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long reporter = userDetails.getId();
        commentService.reportComment(commentNo, reporter, reason);
        return ResponseEntity.ok().build();
    }

    // 댓글 삭제
    @DeleteMapping("/{commentNo}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentNo,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long member = userDetails.getId();
        commentService.deleteComment(commentNo, member);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}
