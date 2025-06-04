package com.pro.service.Impl;

import com.pro.controller.ShowInfoController.CastDTO;
import com.pro.controller.ShowInfoController.ReviewPreviewDTO;
import com.pro.dto.ShowInfoDTO;
import com.pro.entity.*;
import com.pro.repository.*;
import com.pro.service.ShowInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ShowInfoServiceImpl
 * 공연 정보(ShowInfo)와 관련된 조회 및 가공 서비스를 제공
 * 공연 목록, 정렬, 검색, 배우 정보, 후기 미리보기 등 포함
 */
@Service
@RequiredArgsConstructor
public class ShowInfoServiceImpl implements ShowInfoService {

    private final ShowInfoRepository     showInfoRepo;
    private final ShowRepository         showRepo;
    private final ShowLocationRepository locRepo;
    private final ActorShowRepository    actorShowRepo;
    private final PostRepository         postRepo;

    /**
     * 최저가 좌석 가격 계산 헬퍼
     */
    private Integer minPrice(List<Show> ss, java.util.function.ToIntFunction<Show> g) {
        return ss.isEmpty() ? null :
                ss.stream().mapToInt(g).min().stream().boxed().findFirst().orElse(null);
    }

    /**
     * 후기 미리보기 텍스트 자르기 (60자 이상이면 말줄임)
     */
    private String preview(String s){
        return s == null ? "" : s.length() > 60 ? s.substring(0, 60) + "…" : s;
    }

    /**
     * ShowInfo 엔티티 → ShowInfoDTO로 변환
     * 관련 공연 회차, 공연장 정보, 후기 별점, 최저가 등 함께 가공
     */
    private ShowInfoDTO toDTO(ShowInfo si) {

        ShowLocation loc = Optional.ofNullable(si.getLocationId())
                .flatMap(i -> locRepo.findById(i.longValue()))
                .orElse(null);

        List<Show> ss = showRepo.findByShowInfo_Id(si.getId().longValue())
                .stream()
                .filter(s -> s.getShowState() == 0 || s.getShowState() == 1) // 예매 가능/예정
                .toList();

        String start = ss.stream().map(Show::getShowStartTime)
                .min(Comparator.naturalOrder())
                .map(t -> t.toLocalDate().toString()).orElse("");
        String end = ss.stream().map(Show::getShowEndTime)
                .max(Comparator.naturalOrder())
                .map(t -> t.toLocalDate().toString()).orElse("");

        Integer seatA = minPrice(ss, Show::getSeatAPrice);
        Integer seatR = minPrice(ss, Show::getSeatRPrice);
        Integer seatS = minPrice(ss, Show::getSeatSPrice);
        Integer seatVIP = minPrice(ss, Show::getSeatVipPrice);

        Show nearest = ss.stream()
                .filter(s -> s.getShowStartTime().isAfter(java.time.LocalDateTime.now()))
                .min(Comparator.comparing(Show::getShowStartTime))
                .orElse(null);

        double rating = 0.0;
        if (!ss.isEmpty()) {
            List<Long> showNos = ss.stream().map(Show::getShowNo).toList();
            rating = postRepo.findByShow_ShowNoIn(showNos).stream()
                    .mapToInt(Post::getPostRating).average().orElse(0.0);
        }

        return ShowInfoDTO.fromEntity(
                si,
                loc != null ? loc.getName() : "",
                loc != null ? loc.getAddress() : "",
                start, end,
                seatA, seatR, seatS, seatVIP,
                rating,
                nearest
        );
    }

    /**
     * 공연 전체 목록 조회 (관리용 또는 프론트 요청용)
     */
    @Override
    public List<ShowInfoDTO> getAllShowInfos() {
        return showInfoRepo.findAll().stream().map(this::toDTO).toList();
    }

    /**
     * 공연 ID로 상세 조회
     */
    @Override
    public ShowInfoDTO getShowInfo(Integer id) {
        return showInfoRepo.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("ShowInfo not found"));
    }

    /**
     * 공연 정렬 목록 조회 (sort: 컬럼명, dir: asc/desc, ex: 종료 공연 제외 여부)
     */
    @Override
    public Page<ShowInfoDTO> getSortedShowInfoList(String sort, String dir, int p, int s, boolean ex) {
        Sort so = dir.equalsIgnoreCase("desc") ? Sort.by(sort).descending() : Sort.by(sort).ascending();
        Pageable pg = PageRequest.of(p, s, so);
        Page<ShowInfo> data = ex
                ? showInfoRepo.findByShowsShowStateNot(5, pg)
                : showInfoRepo.findAll(pg);
        return new PageImpl<>(data.get().map(this::toDTO).toList(), pg, data.getTotalElements());
    }

    /**
     * 공연명으로 검색
     */
    @Override
    public Page<ShowInfoDTO> searchShowInfos(String k, int p, int s) {
        Pageable pg = PageRequest.of(p, s);
        Page<ShowInfo> d = showInfoRepo.findByNameContainingIgnoreCase(k, pg);
        return new PageImpl<>(d.get().map(this::toDTO).toList(), pg, d.getTotalElements());
    }

    /**
     * 공연 장르로 필터링
     */
    @Override
    public Page<ShowInfoDTO> filterByCategory(String c, int p, int s) {
        Pageable pg = PageRequest.of(p, s);
        Page<ShowInfo> d = showInfoRepo.findByCategoryIgnoreCase(c, pg);
        return new PageImpl<>(d.get().map(this::toDTO).toList(), pg, d.getTotalElements());
    }

    /**
     * 공연 출연 배우 리스트 반환
     */
    @Override
    public List<CastDTO> getCasts(Integer showInfoId) {
        return actorShowRepo.findByShowInfo_Id(showInfoId).stream()
                .map(as -> {
                    Actor a = as.getActor();
                    return new CastDTO(
                            a.getActorNo().longValue(),
                            a.getActorName(),
                            as.getRoleName(),
                            a.getActorImage());
                })
                .toList();
    }

    /**
     * 공연 후기 미리보기 N개 반환 (최신순)
     */
    @Override
    public List<ReviewPreviewDTO> getReviews(Integer showInfoId, int size) {
        List<Long> showNos = showRepo.findByShowInfo_Id(showInfoId.longValue())
                .stream().map(Show::getShowNo).toList();
        if (showNos.isEmpty()) return List.of();

        Pageable firstN = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "postNo"));
        return postRepo.findByShow_ShowNoIn(showNos, firstN).stream()
                .map(p -> new ReviewPreviewDTO(
                        (long) p.getPostNo(),
                        p.getPostTitle(),
                        preview(p.getPostContent()),
                        (long) p.getPostNo()))
                .toList();
    }
}
