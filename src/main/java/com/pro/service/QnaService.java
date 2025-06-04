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

    // 전체 QnA 목록 조회
    public List<QnaResponseDto> getAllQna() {
        return qnaRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate")).stream()
                .map(QnaResponseDto::new)
                .collect(Collectors.toList());
    }

    // 특정 QnA 상세 조회
    public QnaResponseDto getQnaById(Long id) {
        Qna qna = qnaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QnA 글을 찾을 수 없습니다."));
        return new QnaResponseDto(qna);
    }

    // QnA 등록
    public QnaResponseDto createQna(QnaRequestDto dto, Long member) {
        Qna qna = new Qna();
        qna.setMemberNo(member); // 실제 DB 컬럼명과 일치
        qna.setQuestionContents(dto.getQuestionContents());
        qna.setQnaRating(0);
        return new QnaResponseDto(qnaRepository.save(qna));
    }

    // 평점 등록 (작성자 본인만 가능)
    public QnaResponseDto updateRating(Long id, QnaRatingDto dto) {
        Qna qna = qnaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QnA 글을 찾을 수 없습니다."));

        if (!qna.getMemberNo().equals(dto.getMember())) {
            throw new RuntimeException("작성자만 평점을 남길 수 있습니다.");
        }

        qna.setQnaRating(dto.getRating());
        Qna updated = qnaRepository.save(qna);
        return new QnaResponseDto(updated);
    }

    // QnA 삭제 (작성자 본인만 가능)
    @Transactional
    public void deleteQna(Long id, Long member) {
        Qna qna = qnaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("QnA 항목을 찾을 수 없습니다."));

        if (!qna.getMemberNo().equals(member)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        qnaRepository.delete(qna);
    }
}
