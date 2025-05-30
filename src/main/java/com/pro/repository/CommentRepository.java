package com.pro.repository;

import com.pro.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostNo(Long postNo);  // 특정 게시글의 댓글만 조회
    List<Comment> findByMemberOrderByCommentRegisterDateDesc(Long memberId);

}
