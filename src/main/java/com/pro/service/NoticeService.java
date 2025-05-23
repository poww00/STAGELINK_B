package com.pro.service;

import com.pro.dto.NoticeResponseDto;
import com.pro.entity.Notice;
import com.pro.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<NoticeResponseDto> getAllNotices() {
        return noticeRepository.findAll(Sort.by(Sort.Direction.DESC, "noticeDate")).stream()
                .map(NoticeResponseDto::new)
                .collect(Collectors.toList());
    }

    public NoticeResponseDto getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));
        return new NoticeResponseDto(notice);
    }
}

