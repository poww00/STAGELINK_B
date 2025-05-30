package com.pro.controller;

import com.pro.dto.PostRequestDto;
import com.pro.dto.PostResponseDto;
import com.pro.security.user.CustomUserDetails;
import com.pro.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public void createPost(@RequestBody PostRequestDto dto,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long member = userDetails.getId();
        postService.savePost(dto, member);
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
    public void updatePost(@PathVariable int postNo,
                           @RequestBody PostRequestDto dto,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long member = userDetails.getId();
        postService.updatePost(postNo, dto, member);
    }

    @DeleteMapping("/{postNo}")
    public void deletePost(@PathVariable int postNo,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long member = userDetails.getId();
        postService.deletePost(postNo, member);
    }
}
