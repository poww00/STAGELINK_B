package com.pro.repository;

import com.pro.entity.ActorShow;
import com.pro.entity.ActorShowId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ActorShowRepository
 * 배우-공연 관계(출연 정보)를 관리하는 JPA 레포지토리
 * 복합 키(ActorShowId)를 사용하며, 공연 상세 페이지 및 배우 상세 페이지에서 활용됨
 */
public interface ActorShowRepository extends JpaRepository<ActorShow, ActorShowId> {

    // 특정 배우가 출연한 공연 목록 조회
    List<ActorShow> findByActor_ActorNo(Integer actorNo);

    // 특정 공연에 출연한 배우 목록 조회
    List<ActorShow> findByShowInfo_Id(Integer showInfoId);
}
