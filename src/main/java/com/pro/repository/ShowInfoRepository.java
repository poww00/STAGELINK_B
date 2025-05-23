package com.pro.repository;

import com.pro.entity.ShowInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowInfoRepository extends JpaRepository<ShowInfo, Integer> {

    // 종료되지 않은 공연만 조회 (정렬, 페이징 포함)
    Page<ShowInfo> findByShowsShowStateNot(int showState, Pageable pageable);

    // 공연 제목 검색 (대소문자 무시)
    Page<ShowInfo> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    // 카테고리 필터링
    Page<ShowInfo> findByCategoryIgnoreCase(String category, Pageable pageable);
/*
    // 배우명 검색
    @Query(
            "SELECT DISTINCT s FROM ShowInfo s " +
                    "LEFT JOIN s.actorShows ash " +
                    "LEFT JOIN ash.actor a " +
                    "WHERE LOWER(a.actorName) LIKE LOWER(CONCAT('%', :actorName, '%'))"
    )
    Page<ShowInfo> searchByActor(@Param("actorName") String actorName, Pageable pageable);

    // 공연장명 검색
    @Query(
            "SELECT DISTINCT s FROM ShowInfo s " +
                    "LEFT JOIN s.showLocation l " +
                    "WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :locationName, '%'))"
    )
    Page<ShowInfo> searchByLocation(@Param("locationName") String locationName, Pageable pageable);

 */
}
