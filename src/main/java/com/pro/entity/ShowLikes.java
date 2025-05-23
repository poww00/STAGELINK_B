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
    private Integer likeNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHOW_NO", nullable = false)
    private Show show;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER", nullable = false)
    private Member member;
}
