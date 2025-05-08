package com.pro.repository;

import com.pro.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public interface MemberRepository extends JpaRepository<Member, Long> {

    //아이디, 이메일 중복 확인
    boolean existsByUserId(String userId);
    boolean existsByUserEmail(String userEmail);

    // 아이디로 회원 조회
    Member findByUserId(String userId);
}