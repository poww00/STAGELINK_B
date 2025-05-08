package com.pro.yeji.qna.repository;

import com.pro.yeji.qna.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByType(int type);
}
