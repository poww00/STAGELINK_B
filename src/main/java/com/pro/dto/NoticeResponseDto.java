package com.pro.dto;

import com.pro.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponseDto {
    private Long noticeNo;
    private String noticeTitle;
    private String noticeContent;
    private LocalDateTime noticeDate;

    public NoticeResponseDto(Notice notice) {
        this.noticeNo = notice.getNoticeNo();
        this.noticeTitle = notice.getNoticeTitle();
        this.noticeContent = notice.getNoticeContent();
        this.noticeDate = notice.getNoticeDate();
    }
}

