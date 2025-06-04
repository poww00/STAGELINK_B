package com.pro.repository;

import com.pro.entity.ShowLocation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ShowLocationRepository
 * 공연장 정보(ShowLocation)를 처리하는 JPA 레포지토리 인터페이스
 * TBL_SHOWLOCATION 테이블과 매핑된 ShowLocation 엔티티에 대한 기본 CRUD 기능 제공
 */
public interface ShowLocationRepository extends JpaRepository<ShowLocation, Long> {
    // 기본적인 findById, findAll, save, delete 등의 메서드는 JpaRepository에서 상속됨
}
