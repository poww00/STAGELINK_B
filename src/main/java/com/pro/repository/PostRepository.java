package com.pro.repository;

import com.pro.entity.Post;
import com.pro.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByShow_ShowState(int state);
    List<Post> findByShow_ShowState(int state, Sort sort);
    List<Post> findByMemberOrderByPostRegisterDateDesc(Member member);


    /* 모든 후기 전체 */
    List<Post> findByShow_ShowNoIn(List<Long> showNoList);

    /* 특정 개수·정렬로 후기 가져오기 */
    List<Post> findByShow_ShowNoIn(List<Long> showNoList, Pageable pageable);

}
