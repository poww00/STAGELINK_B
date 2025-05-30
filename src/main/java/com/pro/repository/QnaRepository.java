package com.pro.repository;

import com.pro.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna, Long> {
    List<Qna> findByMemberNoOrderByCreateDateDesc(Long memberId);
}
