package com.pro.service;

import com.pro.dto.QnaRatingDto;
import com.pro.dto.QnaRequestDto;
import com.pro.dto.QnaResponseDto;
import com.pro.entity.Qna;
import com.pro.repository.QnaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;

    public List<QnaResponseDto> getAllQna() {
        return qnaRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate")).stream()
                .map(QnaResponseDto::new)
                .collect(Collectors.toList());
    }

    public QnaResponseDto getQnaById(Long id) {
        Qna qna = qnaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QnA 글을 찾을 수 없습니다."));
        return new QnaResponseDto(qna);
    }

    public QnaResponseDto createQna(QnaRequestDto dto, Long memberId) {
        Qna qna = new Qna();
        qna.setMemberNo(memberId);
        qna.setQuestionContents(dto.getQuestionContents());
        qna.setQnaRating(0);  // 기본 평점

        Qna saved = qnaRepository.save(qna);
        return new QnaResponseDto(saved);
    }

    public QnaResponseDto updateRating(Long id, QnaRatingDto dto) {
        Qna qna = qnaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QnA 글을 찾을 수 없습니다."));
        qna.setQnaRating(dto.getRating());
        Qna updated = qnaRepository.save(qna);
        return new QnaResponseDto(updated);
    }

    @Transactional
    public void deleteQna(Long id, Long memberId) {
        Qna qna = qnaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("QnA 항목을 찾을 수 없습니다."));

        if (!qna.getMemberNo().equals(memberId)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        qnaRepository.delete(qna);
    }
}
