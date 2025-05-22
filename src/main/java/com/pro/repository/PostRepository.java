package com.pro.repository;

import com.pro.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;


import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByShow_ShowState(int state);
    List<Post> findByShow_ShowState(int state, Sort sort);
}
