package com.pro.service.Impl;

import com.pro.dto.*;
import com.pro.entity.*;
import com.pro.repository.*;
import com.pro.service.MypageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MyReservationRepository myReservationRepository;
    private final ShowLikesRepository showLikesRepository;
    private final ShowInfoRepository showInfoRepository;
    private final MyLikedShowRepository myLikedShowRepository;

    // 사용자 정보 조회
    @Override
    public MemberInfoDto getMyInfo(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return MemberInfoDto.from(member);
    }

    // 사용자 정보 수정
    @Override
    public void updateMyInfo(Long id, MemberUpdateDto dto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        member.setName(dto.getName());
        member.setNickname(dto.getNickname());
        member.setUserEmail(dto.getUserEmail());
        member.setBirthday(dto.getBirthday());
        member.setGender(dto.getGender());

        memberRepository.save(member);
    }

    // 비밀번호 변경
    @Override
    public void changePassword(Long id, PasswordChangeDto dto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 유효성 검사
        if (!dto.getNewPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$")) {
            throw new IllegalArgumentException("비밀번호는 영문자와 숫자를 포함하여 8~20자여야 합니다.");
        }

        // 비밀번호 인코딩 후 저장
        member.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        memberRepository.save(member);
    }

    // 내정보 수정을 위한 비밀번호 확인
    @Override
    public void verifyPassword(Long id, String password) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다"));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    // 예매 건수 조회
    @Override
    public int countValidReservations(Long id) {
        return myReservationRepository.countValidReservations(id);
    }


    // 예매 내역 리스트 조회
    @Override
    public List<MyReservationDto> getMyReservations(Long id) {
        List<Object[]> rawResults = myReservationRepository.findMyReservations(id);

        return rawResults.stream()
                .map(row -> new MyReservationDto(
                        (String) row[0],
                        (String) row[1],
                        (String) row[2],
                        (String) row[3],
                        ((Number) row[4]).longValue(),
                        (String) row[5],
                        ((Number) row[6]).intValue(),
                        (String) row[7],
                        (String) row[8]

                ))
                .toList();
    }

    // 예매 상세내역 조회
    @Override
    public MyReservationDetailDto getReservationDetail(Long reservationId) {
        MyReservationDetailProjection projection = myReservationRepository.findReservationDetail(reservationId)
                .orElseThrow(() -> new RuntimeException("예매 상세 없음"));

        log.info("예매 상세: {}", projection.getShowTitle());

        // Projection을 DTO로 변환해서 반환
        return MyReservationDetailDto.builder()
                .reservationId(projection.getReservationId())
                .reservationDate(projection.getReservationDate())
                .showTitle(projection.getShowTitle())
                .showDateTime(projection.getShowDateTime())
                .venue(projection.getVenue())
                .seatClass(projection.getSeatClass())
                .seatNumber(projection.getSeatNumber())
                .status(projection.getStatus())
                .buyerName(projection.getBuyerName())
                .cancelAvailableUntil(projection.getCancelAvailableUntil())
                .totalAmount(projection.getTotalAmount())
                .poster(projection.getPoster())
                .build();
    }

    // 찜 목록 조회
    @Override
    public List<MyLikedShowDto> getMyLikedShows(Long memberId) {
        List<Object[]> results = myLikedShowRepository.findMyLikedShows(memberId);

        return results.stream()
                .map(result -> new MyLikedShowDto(
                        (Integer) result[0],
                        (String) result[1],
                        (String) result[2],
                        (String) result[3],
                        Boolean.parseBoolean((String) result[4])
                ))
                .collect(Collectors.toList());
    }

    // 찜 취소
    @Override
    public void deleteMylikedShow(Long id, Integer showInfoId) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"회원 정보를 찾을 수 없습니다."));

        ShowInfo showInfo = showInfoRepository.findById(showInfoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"공연 정보를 찾을 수 없습니다."));

        ShowLikes like = showLikesRepository.findByShowInfoAndMember(showInfo, member)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"찜 내역이 존재하지 않습니다."));

        showLikesRepository.delete(like);
    }

}


