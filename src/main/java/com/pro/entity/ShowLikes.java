package com.pro.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TBL_LIKE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LIKE_NO")
    private Integer likeNo; // 찜 고유 번호 (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHOW_NO", nullable = false)
    private ShowInfo showInfo; // 찜한 공연 정보 (FK → TBL_SHOWINFO)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER", nullable = false)
    private Member member; // 찜한 사용자 (FK → TBL_MEMBER)
}
