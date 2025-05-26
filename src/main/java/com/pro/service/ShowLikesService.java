package com.pro.service;

import com.pro.entity.Member;
import com.pro.entity.Show;
import com.pro.entity.ShowLikes;
import com.pro.repository.ShowLikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 공연 찜하기(Like) 서비스
 * - PK만으로 찜 추가/삭제/확인
 */
@Service
@RequiredArgsConstructor
public class ShowLikesService {

    private final ShowLikesRepository showLikesRepository;

    /**
     * 공연 찜 추가
     */
    @Transactional
    public void addLike(Long showNo, Long userId) {
        Show show = Show.builder().showNo(showNo).build();
        Member member = Member.builder().id(userId).build();

        if (!showLikesRepository.existsByShowAndMember(show, member)) {
            ShowLikes like = ShowLikes.builder().show(show).member(member).build();
            showLikesRepository.save(like);
        }
    }

    /**
     * 공연 찜 삭제
     */
    @Transactional
    public void removeLike(Long showNo, Long userId) {
        Show show = Show.builder().showNo(showNo).build();
        Member member = Member.builder().id(userId).build();

        showLikesRepository.deleteByShowAndMember(show, member);
    }

    /**
     * 찜 여부 확인
     */
    @Transactional(readOnly = true)
    public boolean isLiked(Long showNo, Long userId) {
        Show show = Show.builder().showNo(showNo).build();
        Member member = Member.builder().id(userId).build();

        return showLikesRepository.existsByShowAndMember(show, member);
    }
}
