package com.pro.repository;

import com.pro.entity.Member;
import com.pro.entity.Show;
import com.pro.entity.ShowLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShowLikesRepository extends JpaRepository<ShowLikes, Integer> {
    Optional<ShowLikes> findByShowAndMember(Show show, Member member);

    void deleteByShowAndMember(Show show, Member member);

    boolean existsByShowAndMember(Show show, Member member);

    List<ShowLikes> findAllByMember(Member member);
}
