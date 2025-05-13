package com.pro.repository;

import com.pro.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@ResponseBody
public interface MemberRepository extends JpaRepository<Member, Long> {

    //아이디, 이메일 중복 확인
    boolean existsByUserId(String userId);
    boolean existsByUserEmail(String userEmail);


    Optional<Member> findByUserId(String username);
}