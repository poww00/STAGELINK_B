package com.pro.repository;

import com.pro.entity.Member;
import com.pro.entity.Show;
import com.pro.entity.ShowInfo;
import com.pro.entity.ShowLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * ShowLikesRepository
 * 공연 찜 정보(ShowLikes)를 처리하는 JPA 레포지토리 인터페이스
 * TBL_LIKE 테이블과 매핑된 ShowLikes 엔티티에 대한 CRUD 및 사용자별 찜 기능 제공
 */
public interface ShowLikesRepository extends JpaRepository<ShowLikes, Integer> {

     //특정 공연과 회원의 찜 정보 조회
     Optional<ShowLikes> findByShowInfoAndMember(ShowInfo showInfo, Member member);

     //특정 공연과 회원의 찜 정보 삭제
     void deleteByShowInfoAndMember(ShowInfo showInfo, Member member);

    // 특정 공연에 대해 사용자가 찜했는지 여부 확인
    boolean existsByShowInfoAndMember(ShowInfo showInfo, Member member);

    // 특정 사용자가 찜한 모든 공연 목록 조회
    List<ShowLikes> findAllByMember(Member member);

}
