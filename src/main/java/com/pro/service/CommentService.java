package com.pro.service;

import com.pro.dto.CommentRequestDto;
import com.pro.dto.CommentResponseDto;
import com.pro.entity.Comment;
import com.pro.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository; // 댓글 관련 DB 작업을 담당하는 리포지토리

    // 특정 게시글(postNo)에 달린 모든 댓글을 조회하는 메서드
    public List<CommentResponseDto> getCommentsByPost(Long postNo) {
        return commentRepository.findByPostNo(postNo).stream()
                .map(CommentResponseDto::new) // Comment -> CommentResponseDto로 변환
                .collect(Collectors.toList());
    }

    // 댓글 작성 메서드
    public CommentResponseDto createComment(CommentRequestDto dto) {
        Comment comment = new Comment();
        comment.setPostNo(dto.getPostNo());         // 댓글이 달린 게시글 번호
        comment.setMemberNo(dto.getMemberNo());     // 댓글 작성자 회원 번호
        comment.setContent(dto.getContent());       // 댓글 내용
        comment.setNickname(dto.getNickname()); // 닉네임 저장


        comment = commentRepository.save(comment);  // DB에 저장

        return new CommentResponseDto(comment);     // 저장된 댓글을 DTO로 변환해서 반환
    }

    // 댓글 삭제 메서드
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id) // 해당 ID의 댓글이 있는지 조회
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        commentRepository.delete(comment); // 댓글 삭제
    }
}
