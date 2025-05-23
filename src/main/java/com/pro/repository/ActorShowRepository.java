package com.pro.repository;

import com.pro.entity.ActorShow;
import com.pro.entity.ActorShowId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActorShowRepository extends JpaRepository<ActorShow, ActorShowId> {
    List<ActorShow> findByActor_ActorNo(Integer actorNo);
}
