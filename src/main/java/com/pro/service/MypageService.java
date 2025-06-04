package com.pro.service;

import com.pro.dto.*;

import java.util.List;

public interface MypageService {

    MemberInfoDto getMyInfo(Long id);
    // 내정보 변경
    void updateMyInfo(Long id, MemberUpdateDto dto);

    // 비밀번호 변경
    void changePassword(Long id, PasswordChangeDto dto);

    // 내정보 수정을 위한 비밀번호 확인
    void verifyPassword(Long id, String password);

    // 예매 건수 조회
    int countValidReservations(Long id);

    // 예매내역 조회
    List<MyReservationDto> getMyReservations(Long id);

    // 예매 상세 조회
    ReservationDetailDto getReservationDetail(Long id);

    // 찜 목록 조회
    List<MylikedShowDto> getLikedShows(Long id);
}
