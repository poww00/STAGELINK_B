package com.pro.service;

import com.pro.dto.PostRequestDto;
import com.pro.dto.PostResponseDto;
import com.pro.entity.Post;
import com.pro.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 게시글 전체 조회
    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    // 게시글 상세 조회
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return new PostResponseDto(post);
    }

    // 게시글 작성
    public PostResponseDto createPost(PostRequestDto dto) {
        Post post = new Post();
        post.setMemberNo(dto.getMemberNo());
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setRating(dto.getRating());
        post.setNickname(dto.getNickname()); // 닉네임 저장
        post = postRepository.save(post);
        return new PostResponseDto(post);

    }

    // 게시글 삭제
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        postRepository.delete(post);
    }

    // 게시글 수정
    public PostResponseDto updatePost(Long id, PostRequestDto dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        Post updatedPost = postRepository.save(post);
        return new PostResponseDto(updatedPost);
    }

}
