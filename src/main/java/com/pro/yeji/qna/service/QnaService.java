package com.pro.yeji.qna.service;

import com.pro.yeji.qna.dto.QnaRequestDto;
import com.pro.yeji.qna.dto.QnaResponseDto;
import com.pro.yeji.qna.entity.Post;
import com.pro.yeji.qna.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QnaService {
    private final PostRepository postRepository;

    public QnaService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 전체 QnA 조회
    public List<QnaResponseDto> getAllQna() {
        return postRepository.findByType(2).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // QnA 단건 조회
    public QnaResponseDto getQnaById(Long id) {
        Post post = postRepository.findById(id)
                .filter(p -> p.getType() == 2)
                .orElseThrow(() -> new RuntimeException("QnA 글을 찾을 수 없습니다."));
        return toDto(post);
    }

    // QnA 등록
    public QnaResponseDto createQna(QnaRequestDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setMember(dto.getMember());
        post.setType(2);
        post.setRegisterDate(LocalDateTime.now());
        Post saved = postRepository.save(post);
        return toDto(saved);
    }

    // QnA 삭제
    public void deleteQna(Long id) {
        Post post = postRepository.findById(id)
                .filter(p -> p.getType() == 2)
                .orElseThrow(() -> new RuntimeException("QnA 글을 찾을 수 없습니다."));
        postRepository.delete(post);
    }

    // 엔티티 → DTO 변환
    private QnaResponseDto toDto(Post post) {
        QnaResponseDto dto = new QnaResponseDto();
        dto.setPostNo(post.getPostNo());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setRegisterDate(post.getRegisterDate());
        return dto;
    }

    // Qna 별점 평가
    public void updateRating(Long id, Integer rating) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("별점은 1~5 사이여야합니다.");
        }

        Post post = postRepository.findById(id)
                .filter(p -> p.getType() == 2)
                .orElseThrow(() -> new RuntimeException("QnA 글을 찾을 수 없습니다."));

        post.setRating(rating);
        postRepository.save(post);
    }
}
