package com.pro.repository;

import com.pro.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ActorRepository
 * 배우(Actor) 정보를 처리하는 JPA 레포지토리
 * 기본적인 CRUD 기능 제공 (TBL_ACTOR 테이블과 매핑)
 */
public interface ActorRepository extends JpaRepository<Actor, Integer> {
    // 기본 메서드: findById, findAll, save, delete 등 JpaRepository에서 제공
}
