package com.pro.service;

import com.pro.dto.QnaRatingDto;
import com.pro.dto.QnaRequestDto;
import com.pro.dto.QnaResponseDto;
import com.pro.entity.Qna;
import com.pro.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;

    public List<QnaResponseDto> getAllQna() {
        return qnaRepository.findAll().stream()
                .map(QnaResponseDto::new)
                .collect(Collectors.toList());
    }

    public QnaResponseDto getQnaById(Long id) {
        Qna qna = qnaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QnA 글을 찾을 수 없습니다."));
        return new QnaResponseDto(qna);
    }

    public QnaResponseDto createQna(QnaRequestDto dto) {
        Qna qna = new Qna();
        qna.setMemberNo(dto.getMemberNo());
        qna.setQuestionContents(dto.getQuestionContents());
        Qna saved = qnaRepository.save(qna);
        return new QnaResponseDto(saved);
    }

    public QnaResponseDto updateRating(Long id, QnaRatingDto dto) {
        Qna qna = qnaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QnA 글을 찾을 수 없습니다."));
        qna.setRating(dto.getRating());
        Qna updated = qnaRepository.save(qna);
        return new QnaResponseDto(updated);
    }
}
