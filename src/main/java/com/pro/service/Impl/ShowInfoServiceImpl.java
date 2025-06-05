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

@Service
@RequiredArgsConstructor
public class ShowInfoServiceImpl implements ShowInfoService {

    private final ShowInfoRepository     showInfoRepo;
    private final ShowRepository         showRepo;
    private final ShowLocationRepository locRepo;
    private final ActorShowRepository    actorShowRepo;
    private final PostRepository         postRepo;

    /**
     * 각 좌석 등급별 최저가 반환 (없으면 null)
     */
    private Integer minPrice(List<Show> ss, java.util.function.ToIntFunction<Show> g) {
        return ss.isEmpty() ? null : ss.stream().mapToInt(g).min().stream().boxed().findFirst().orElse(null);
    }

    /**
     * 후기 미리보기용 텍스트 자르기 (60자 제한)
     */
    private String preview(String s){
        return s == null ? "" : s.length() > 60 ? s.substring(0, 60) + "…" : s;
    }

    /**
     * ShowInfo → ShowInfoDTO로 변환 (위치명, 공연기간, 좌석가격, 별점 등 포함)
     */
    private ShowInfoDTO toDTO(ShowInfo si) {
        ShowLocation loc = Optional.ofNullable(si.getLocationId())
                .flatMap(id -> locRepo.findById(id.longValue()))
                .orElse(null);

        List<Show> sessions = showRepo.findByShowInfo_Id(si.getId().longValue())
                .stream()
                .filter(s -> s.getShowState() == 0 || s.getShowState() == 1) // 판매예정 또는 판매중
                .toList();

        String periodStart = sessions.stream().map(Show::getShowStartTime)
                .min(Comparator.naturalOrder())
                .map(t -> t.toLocalDate().toString()).orElse("");
        String periodEnd = sessions.stream().map(Show::getShowEndTime)
                .max(Comparator.naturalOrder())
                .map(t -> t.toLocalDate().toString()).orElse("");

        Integer seatA = minPrice(sessions, Show::getSeatAPrice);
        Integer seatR = minPrice(sessions, Show::getSeatRPrice);
        Integer seatS = minPrice(sessions, Show::getSeatSPrice);
        Integer seatV = minPrice(sessions, Show::getSeatVipPrice);

        Show nearest = sessions.stream()
                .filter(s -> s.getShowStartTime().isAfter(java.time.LocalDateTime.now()))
                .min(Comparator.comparing(Show::getShowStartTime))
                .orElse(null);

        double rating = 0.0;
        if (!sessions.isEmpty()) {
            List<Long> showNos = sessions.stream().map(Show::getShowNo).toList();
            rating = postRepo.findByShow_ShowNoIn(showNos).stream()
                    .mapToInt(Post::getPostRating).average().orElse(0.0);
        }

        return ShowInfoDTO.fromEntity(
                si,
                loc != null ? loc.getName() : "",
                loc != null ? loc.getAddress() : "",
                periodStart, periodEnd,
                seatA, seatR, seatS, seatV,
                rating,
                nearest
        );
    }

    /** 전체 공연 리스트 조회 */
    @Override
    public List<ShowInfoDTO> getAllShowInfos() {
        return showInfoRepo.findAll().stream().map(this::toDTO).toList();
    }

    /** 공연 상세 정보 조회 */
    @Override
    public ShowInfoDTO getShowInfo(Integer id) {
        return showInfoRepo.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("ShowInfo not found"));
    }

    /** 공연 정렬 리스트 조회 (이름순, 날짜순 등) */
    @Override
    public Page<ShowInfoDTO> getSortedShowInfoList(String sort, String dir, int p, int s, boolean ex) {
        List<ShowInfo> rawList = ex
                ? showInfoRepo.findDistinctByShowStateNot(5, Pageable.unpaged()).getContent()
                : showInfoRepo.findAll();

        List<ShowInfoDTO> dtoList = rawList.stream().map(this::toDTO).toList();

        // 정렬 조건 처리
        Comparator<ShowInfoDTO> comp;
        if ("periodStart".equals(sort)) {
            comp = Comparator.comparing(dto -> {
                try {
                    return java.time.LocalDate.parse(dto.getPeriodStart());
                } catch (Exception e) {
                    return java.time.LocalDate.MAX;
                }
            });
        } else {
            comp = Comparator.comparing(ShowInfoDTO::getName, String.CASE_INSENSITIVE_ORDER);
        }
        if ("desc".equalsIgnoreCase(dir)) comp = comp.reversed();

        dtoList = dtoList.stream().sorted(comp).toList();
        int start = p * s;
        int end = Math.min(start + s, dtoList.size());
        List<ShowInfoDTO> paged = dtoList.subList(start, end);

        return new PageImpl<>(paged, PageRequest.of(p, s), dtoList.size());
    }

    /** 🔍 검색 기능: 공연명에 키워드 포함 + 회차 존재 필수 */
    @Override
    public Page<ShowInfoDTO> searchShowInfos(String keyword, int p, int s) {
        Pageable pg = PageRequest.of(p, s);
        Page<ShowInfo> data = showInfoRepo.searchOnlyWithShow(keyword, pg);
        return new PageImpl<>(data.get().map(this::toDTO).toList(), pg, data.getTotalElements());
    }

    /** 카테고리 필터링 */
    @Override
    public Page<ShowInfoDTO> filterByCategory(String category, int p, int s) {
        Pageable pg = PageRequest.of(p, s);
        Page<ShowInfo> data = showInfoRepo.findByCategoryIgnoreCase(category, pg);
        return new PageImpl<>(data.get().map(this::toDTO).toList(), pg, data.getTotalElements());
    }

    /** 출연 배우 리스트 반환 */
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
                }).toList();
    }

    /** 후기 미리보기 리스트 반환 (최신순 size개) */
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
