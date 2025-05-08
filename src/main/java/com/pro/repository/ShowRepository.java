package com.pro.repository;

import com.pro.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    // 필요 시 커스텀 쿼리 추가 가능
}

