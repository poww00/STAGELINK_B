package com.pro.controller;

import com.pro.dto.CommentResponseDto;
import com.pro.dto.PostResponseDto;
import com.pro.dto.QnaResponseDto;
import com.pro.service.MyActivityPageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyActivityPageController {

    private final MyActivityPageService myActivityPageService;

    // 내가 작성한 게시글 조회
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> getMyPosts(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberNo");
        List<PostResponseDto> myPosts = myActivityPageService.getMyPosts(memberId);
        return ResponseEntity.ok(myPosts);
    }

    // 내가 작성한 게시글 삭제
    @DeleteMapping("/posts/{postNo}")
    public ResponseEntity<Void> deleteMyPost(@PathVariable int postNo, HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberNo");
        myActivityPageService.deleteMyPost(postNo, memberId);
        return ResponseEntity.noContent().build();
    }

    // 내가 작성한 댓글 조회
    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponseDto>> getMyComments(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberNo");
        List<CommentResponseDto> myComments = myActivityPageService.getMyComments(memberId);
        return ResponseEntity.ok(myComments);
    }

    // 내가 작성한 댓글 삭제
    @DeleteMapping("/comments/{commentNo}")
    public ResponseEntity<Void> deleteMyComment(@PathVariable Long commentNo, HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberNo");
        myActivityPageService.deleteMyComment(commentNo, memberId);
        return ResponseEntity.noContent().build();
    }

    // 내가 작성한 QnA 조회
    @GetMapping("/qna")
    public ResponseEntity<List<QnaResponseDto>> getMyQna(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberNo");
        List<QnaResponseDto> myQna = myActivityPageService.getMyQna(memberId);
        return ResponseEntity.ok(myQna);
    }

}
