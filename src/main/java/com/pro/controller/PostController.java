package com.pro.controller;

import com.pro.dto.PostRequestDto;
import com.pro.dto.PostResponseDto;
import com.pro.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public void createPost(@RequestBody PostRequestDto dto, HttpServletRequest request) {
        Long memberId = Long.valueOf((int) request.getAttribute("memberNo"));
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
}
