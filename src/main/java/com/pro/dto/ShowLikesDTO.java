package com.pro.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowLikesDTO {
    private Integer likeNo; // 찜 고유 번호 (PK)
    private Integer showNo; // 찜한 공연 번호 (TBL_SHOWINFO의 PK)
    private Integer userId; // 찜한 사용자 ID (
}
