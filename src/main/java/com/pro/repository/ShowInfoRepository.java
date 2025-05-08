package com.pro.repository;

import com.pro.entity.ShowInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShowInfoRepository extends JpaRepository<ShowInfo, Long>, JpaSpecificationExecutor<ShowInfo> {

    // 종료되지 않은 공연만 조회 (정렬, 페이징 포함)
    Page<ShowInfo> findByShowsShowStateNot(int showState, Pageable pageable);

    // 공연 제목 검색 (대소문자 무시)
    Page<ShowInfo> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    // 카테고리 필터링
    Page<ShowInfo> findByCategoryIgnoreCase(String category, Pageable pageable);
}
