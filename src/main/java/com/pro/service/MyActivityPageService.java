package com.pro.service;

import com.pro.dto.CommentResponseDto;
import com.pro.dto.PostResponseDto;
import com.pro.dto.QnaResponseDto;
import com.pro.entity.Comment;
import com.pro.entity.Member;
import com.pro.entity.Post;
import com.pro.entity.Qna;
import com.pro.repository.CommentRepository;
import com.pro.repository.MemberRepository;
import com.pro.repository.PostRepository;
import com.pro.repository.QnaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyActivityPageService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final QnaRepository qnaRepository;
    private final MemberRepository memberRepository;

    // 내가 쓴 게시글 조회
    public List<PostResponseDto> getMyPosts(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 존재하지 않습니다."));

        return postRepository.findByMemberOrderByPostRegisterDateDesc(member).stream()
                .map(post -> new PostResponseDto(
                        post.getPostNo(),
                        post.getPostTitle(),
                        post.getPostContent(),
                        post.getPostRating(),
                        post.getNickname(),
                        post.getPostRegisterDate() != null ? post.getPostRegisterDate().toString() : null,
                        post.getShow() != null && post.getShow().getShowInfo() != null
                                ? post.getShow().getShowInfo().getName() : null,
                        post.getMember().getId()
                ))
                .collect(Collectors.toList());
    }

    // 내가 쓴 게시글 삭제
    @Transactional
    public void deleteMyPost(int postNo, Long memberId) {
        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!memberId.equals(post.getMember().getId())) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    // 내가 쓴 댓글 조회
    public List<CommentResponseDto> getMyComments(Long memberId) {
        return commentRepository.findByMemberOrderByCommentRegisterDateDesc(memberId)
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    // 내가 쓴 댓글 삭제
    @Transactional
    public void deleteMyComment(Long commentNo, Long memberId) {
        Comment comment = commentRepository.findById(commentNo)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!memberId.equals(comment.getMember())) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

    // 내가 쓴 QnA 조회
    public List<QnaResponseDto> getMyQna(Long memberId) {
        return qnaRepository.findByMemberNoOrderByCreateDateDesc(memberId).stream()
                .map(QnaResponseDto::new)
                .collect(Collectors.toList());
    }
}
