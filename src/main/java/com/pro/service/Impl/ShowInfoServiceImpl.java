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

    /* ──────────── 헬퍼 메서드들 ──────────── */

    private Integer minPrice(List<Show> ss, java.util.function.ToIntFunction<Show> g) {
        return ss.isEmpty() ? null :
                ss.stream().mapToInt(g).min().stream().boxed().findFirst().orElse(null);
    }

    private String preview(String s){
        return s == null ? "" : s.length() > 60 ? s.substring(0, 60) + "…" : s;
    }

    private ShowInfoDTO toDTO(ShowInfo si) {

        ShowLocation loc = Optional.ofNullable(si.getLocationId())
                .flatMap(id -> locRepo.findById(id.longValue()))
                .orElse(null);

        List<Show> sessions = showRepo.findByShowInfo_Id(si.getId().longValue())
                .stream()
                .filter(s -> s.getShowState() == 0 || s.getShowState() == 1)  // 판매예정·판매중만
                .toList();

        String periodStart = sessions.stream().map(Show::getShowStartTime)
                .min(Comparator.naturalOrder())
                .map(t -> t.toLocalDate().toString()).orElse("");
        String periodEnd = sessions.stream().map(Show::getShowEndTime)
                .max(Comparator.naturalOrder())
                .map(t -> t.toLocalDate().toString()).orElse("");

        Integer seatA  = minPrice(sessions, Show::getSeatAPrice);
        Integer seatR  = minPrice(sessions, Show::getSeatRPrice);
        Integer seatS  = minPrice(sessions, Show::getSeatSPrice);
        Integer seatV  = minPrice(sessions, Show::getSeatVipPrice);

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

    /* ──────────── 서비스 구현 ──────────── */

    @Override
    public List<ShowInfoDTO> getAllShowInfos() {
        return showInfoRepo.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public ShowInfoDTO getShowInfo(Integer id) {
        return showInfoRepo.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("ShowInfo not found"));
    }

    /** 공연 정렬 목록 조회 */
    @Override
    public Page<ShowInfoDTO> getSortedShowInfoList(String sort, String dir,
                                                   int p, int s, boolean ex) {

        /* ✅ (수정) Pageable.unpaged() 전달 */
        List<ShowInfo> rawList = ex
                ? showInfoRepo.findDistinctByShowStateNot(5, Pageable.unpaged()).getContent()
                : showInfoRepo.findAll();

        List<ShowInfoDTO> dtoList = rawList.stream().map(this::toDTO).toList();

        /* 수동 정렬 */
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
        int end   = Math.min(start + s, dtoList.size());
        List<ShowInfoDTO> paged = dtoList.subList(start, end);

        return new PageImpl<>(paged, PageRequest.of(p, s), dtoList.size());
    }

    @Override
    public Page<ShowInfoDTO> searchShowInfos(String k, int p, int s) {
        Pageable pg = PageRequest.of(p, s);
        Page<ShowInfo> data = showInfoRepo.findByNameContainingIgnoreCase(k, pg);
        return new PageImpl<>(data.get().map(this::toDTO).toList(), pg, data.getTotalElements());
    }

    @Override
    public Page<ShowInfoDTO> filterByCategory(String c, int p, int s) {
        Pageable pg = PageRequest.of(p, s);
        Page<ShowInfo> data = showInfoRepo.findByCategoryIgnoreCase(c, pg);
        return new PageImpl<>(data.get().map(this::toDTO).toList(), pg, data.getTotalElements());
    }

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
