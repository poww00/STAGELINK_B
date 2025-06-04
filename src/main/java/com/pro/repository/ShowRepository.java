package com.pro.repository;

import com.pro.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ShowRepository
 * 공연 회차 정보를 처리하는 JPA 레포지토리 인터페이스
 * TBL_SHOW 테이블과 매핑된 Show 엔티티에 대한 CRUD 및 커스텀 조회 메서드 제공
 */
public interface ShowRepository extends JpaRepository<Show, Long> {

    /**
     * 특정 공연 정보(ShowInfo)에 해당하는 모든 회차(Show) 목록 조회
     * @param showInfoId 공연 정보 ID (TBL_SHOWINFO의 PK)
     * @return 해당 공연에 등록된 회차 리스트
     */
    List<Show> findByShowInfo_Id(Long showInfoId);
}

