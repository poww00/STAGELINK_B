package com.pro.repository;

import com.pro.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@ResponseBody
public interface  MemberRepository extends JpaRepository<Member, Long> {

    //아이디, 이메일 중복 확인
    boolean existsByUserId(String userId);
    boolean existsByUserEmail(String userEmail);

    // 로그인 등 기본 조회
    Optional<Member> findByUserId(String username);
    Optional<Member> findByUserEmail(String userEmail);


    // 아이디 찾기용
    Optional<Member> findByNameAndUserEmail(String name, String userEmail);
}