package com.pro.controller.member;

import com.pro.dto.*;
import com.pro.security.user.CustomUserDetails;
import com.pro.service.MemberService;
import com.pro.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;
    private final MemberService memberService;

    @GetMapping("/home")
    public ResponseEntity<MypageHomeDto> getMypageHome(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        String nickname = userDetails.getNickname();

        int reservationCount = mypageService.countValidReservations(userId);

        MypageHomeDto dto = MypageHomeDto.builder()
                .nickname(nickname)
                .reservationCount(reservationCount)
                .build();

        return ResponseEntity.ok(dto);
    }

    // 비밀번호 확인
    @PostMapping("/confirm-password")
    public ResponseEntity<?> confirmPassword(@RequestBody PasswordConfirmRequestDto dto,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        mypageService.verifyPassword(userDetails.getId(), dto.getPassword());
        return ResponseEntity.ok("비밀번호가 확인되었습니다.");
    }



    // 내 정보 조회
    @GetMapping("/info")
    public ResponseEntity<MemberInfoDto> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MemberInfoDto dto = mypageService.getMyInfo(userDetails.getId());
        return ResponseEntity.ok(dto);
    }

    // 내 정보 수정
    @PutMapping("/update")
    public ResponseEntity<?> updateMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @RequestBody MemberUpdateDto dto) {
        mypageService.updateMyInfo(userDetails.getId(), dto);
        return ResponseEntity.ok("회원 정보가 수정되었습니다.");
    }

    // 사용자 비밀번호 변경
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeDto dto,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String id = userDetails.getUserId();
        mypageService.changePassword(userDetails.getId(), dto);
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }

    // 예매내역 조회
    @GetMapping("/reservations")
    public ResponseEntity<List<MyReservationDto>> getMyReservations(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = userDetails.getId();
        List<MyReservationDto> reservations = mypageService.getMyReservations(id);

        return ResponseEntity.ok(reservations);
    }

    // 예매 상세 조회
    @GetMapping("/reservations/{reservationId}")
    public ResponseEntity<MyReservationDetailDto> getReservationDetail(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 인증된 사용자 Id
        Long memberId = userDetails.getId();

        MyReservationDetailDto dto = mypageService.getReservationDetail(reservationId);
        return ResponseEntity.ok(dto);
    }

    // 찜 목록 조회
    @GetMapping("/likes")
    public ResponseEntity<List<MylikedShowDto>> getMylikedShows(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getId();
        List<MylikedShowDto> likedShows = mypageService.getLikedShows(memberId);
        return ResponseEntity.ok(likedShows);
    }

    // 회원 탈퇴 가능한지 미리 확인
    @GetMapping("/check-can-withdraw")
    public ResponseEntity<?> checkCanWithdraw(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getId();
        if (memberService.hasReservation(memberId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("예매 내역이 있어 탈퇴할 수 없습니다.");
        }
        return ResponseEntity.ok().build();
    }
    // 실제 탈퇴 처리
    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdrawAccount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getId();
        memberService.withdrawMember(memberId);
        return ResponseEntity.ok("회원 탈퇴 완료");
    }
}