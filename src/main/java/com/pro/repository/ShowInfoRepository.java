package com.pro.repository;

import com.pro.entity.ShowInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface ShowInfoRepository extends JpaRepository<ShowInfo, Integer> {

    // 종료되지 않은 공연만 조회
    @Query("SELECT DISTINCT si FROM ShowInfo si JOIN si.shows s WHERE s.showState <> :showState")
    Page<ShowInfo> findDistinctByShowStateNot(@Param("showState") int showState, Pageable pageable);

    // 공연 제목 검색 (모든 ShowInfo 포함)
    Page<ShowInfo> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    // 🔹 공연 제목 검색: 회차가 존재하는 ShowInfo만 검색
    @Query("SELECT DISTINCT si FROM ShowInfo si JOIN si.shows s " +
            "WHERE LOWER(si.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ShowInfo> searchOnlyWithShow(@Param("keyword") String keyword, Pageable pageable);

    // 카테고리 필터링
    Page<ShowInfo> findByCategoryIgnoreCase(String category, Pageable pageable);
}
