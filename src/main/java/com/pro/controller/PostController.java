package com.pro.controller;

import com.pro.dto.PostRequestDto;
import com.pro.dto.PostResponseDto;
import com.pro.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final boolean devMode = true; // ✅ 테스트용 모드

    @PostMapping
    public void createPost(@RequestBody PostRequestDto dto, HttpServletRequest request) {
        Long memberId;
        Object memberNoAttr = request.getAttribute("memberNo");

        if (devMode || memberNoAttr == null) {
            memberId = 1003L; // ✅ 테스트용 memberNo
        } else {
            memberId = Long.valueOf((int) memberNoAttr);
        }

        postService.savePost(dto, memberId);
    }

    @GetMapping
    public List<PostResponseDto> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{postNo}")
    public PostResponseDto getPostDetail(@PathVariable int postNo) {
        return postService.getPostById(postNo);
    }

    @GetMapping("/state/{state}")
    public List<PostResponseDto> getPostsByState(@PathVariable int state) {
        return postService.getPostsByShowState(state);
    }

    @PutMapping("/{postNo}")
    public void updatePost(@PathVariable int postNo, @RequestBody PostRequestDto dto, HttpServletRequest request) {
        Long memberId;
        Object memberNoAttr = request.getAttribute("memberNo");

        if (devMode || memberNoAttr == null) {
            memberId = 1003L;
        } else {
            memberId = Long.valueOf((int) memberNoAttr);
        }

        postService.updatePost(postNo, dto, memberId);
    }

    @DeleteMapping("/{postNo}")
    public void deletePost(@PathVariable int postNo, HttpServletRequest request) {
        Long memberId;
        Object memberNoAttr = request.getAttribute("memberNo");

        if (devMode || memberNoAttr == null) {
            memberId = 1003L;
        } else {
            memberId = Long.valueOf((int) memberNoAttr);
        }

        postService.deletePost(postNo, memberId, devMode); // ✅ devMode 넘겨줌
    }
}
