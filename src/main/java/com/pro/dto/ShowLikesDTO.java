package com.pro.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowLikesDTO {
    private Integer likeNo;
    private Integer showNo;
    private Integer userId; // memberNo → userId로 변경
}
