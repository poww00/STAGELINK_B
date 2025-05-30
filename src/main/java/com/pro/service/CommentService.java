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

    private final CommentRepository commentRepository;

    // 댓글 작성
    public CommentResponseDto createComment(CommentRequestDto dto, Long member, String nickname) {
        Comment comment = new Comment();
        comment.setPostNo(dto.getPostNo());
        comment.setMember(member);
        comment.setNickname(nickname); // 로그인한 사용자 닉네임
        comment.setCommentContent(dto.getCommentContent());
        comment.setCommentRating(dto.getCommentRating());
        comment.setCommentReportCount(0);

        Comment saved = commentRepository.save(comment);
        return new CommentResponseDto(saved);
    }


    // 게시글별 댓글 목록 조회
    public List<CommentResponseDto> getCommentsByPostNo(Long postNo) {
        return commentRepository.findByPostNo(postNo).stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    // 댓글 신고
    public void reportComment(Long commentNo, Long reporter, String reason) {
        Comment comment = commentRepository.findById(commentNo)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        // 신고자 또는 사유는 현재 저장하지 않지만, 필요 시 신고 테이블로 확장 가능
        comment.setCommentReportCount(comment.getCommentReportCount() + 1);
        commentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long commentNo, Long member) {
        Comment comment = commentRepository.findById(commentNo)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        if (!comment.getMember().equals(member)) {
            throw new RuntimeException("작성자만 댓글을 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }
}
