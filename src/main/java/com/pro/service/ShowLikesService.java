package com.pro.service;

import com.pro.entity.Member;
import com.pro.entity.ShowInfo;
import com.pro.entity.ShowLikes;
import com.pro.repository.MemberRepository;
import com.pro.repository.ShowInfoRepository;
import com.pro.repository.ShowLikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 공연 찜하기(Like) 서비스
 * - 찜 추가/삭제/확인
 */
@Service
@RequiredArgsConstructor
public class ShowLikesService {

    private final ShowLikesRepository showLikesRepository;
    private final ShowInfoRepository showInfoRepository;
    private final MemberRepository memberRepository;

    /**
     * 공연 찜 추가
     */
    @Transactional
    public void addLike(Integer showInfoId, Long userId) {
        // 실제 존재하는 ShowInfo 엔티티 조회
        ShowInfo showInfo = showInfoRepository.findById(showInfoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연 정보입니다.showInfoId=" + showInfoId));

        // 실제 존재하는 Member 엔티티 조회
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다.userId=" + userId));

        // 엔티티 존재 여부 체크 후 저장
        if (!showLikesRepository.existsByShowInfoAndMember(showInfo, member)) {
            ShowLikes like = new ShowLikes();
            like.setShowInfo(showInfo);
            like.setMember(member);
            showLikesRepository.save(like);
        }
    }


    /**
     * 공연 찜 삭제
     */
    @Transactional
    public void removeLike(Integer showInfoId, Long userId) {
        ShowInfo showInfo = showInfoRepository.findById(showInfoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연 정보입니다. showInfoId=" + showInfoId));

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. userId=" + userId));

        showLikesRepository.deleteByShowInfoAndMember(showInfo, member);
    }

    /**
     * 찜 여부 확인
     */
    @Transactional(readOnly = true)
    public boolean isLiked(Integer showInfoId, Long userId) {
        ShowInfo showInfo = showInfoRepository.findById(showInfoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연 정보입니다. showInfoId=" + showInfoId));

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. userId=" + userId));

        return showLikesRepository.existsByShowInfoAndMember(showInfo, member);
    }
}
