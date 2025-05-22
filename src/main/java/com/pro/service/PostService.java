package com.pro.service;

import com.pro.dto.PostRequestDto;
import com.pro.dto.PostResponseDto;
import com.pro.entity.Member;
import com.pro.entity.Post;
import com.pro.entity.Show;
import com.pro.repository.MemberRepository;
import com.pro.repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager em;

    /**
     * 게시글 저장
     */
    @Transactional
    public void savePost(PostRequestDto dto, Long memberId) {
        try {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

            Show show = em.createQuery("SELECT s FROM Show s WHERE s.showNo = :no", Show.class)
                    .setParameter("no", dto.getPostShowNo())
                    .getSingleResult();

            Post post = new Post();
            post.setMember(member); // 컬럼명은 member (기존 member_no 아님)
            post.setNickname(member.getNickname());
            post.setShow(show);
            post.setPostTitle(dto.getPostTitle());
            post.setPostContent(dto.getPostContent());
            post.setPostRating(dto.getPostRating());
            post.setPostRegisterDate(LocalDateTime.now());
            post.setPostReportCount(0);

            postRepository.save(post);

        } catch (Exception e) {
            throw new RuntimeException("게시글 저장 실패", e);
        }
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public void updatePost(int postNo, PostRequestDto dto, Long memberId) {
        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        if (!post.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        post.setPostTitle(dto.getPostTitle());
        post.setPostContent(dto.getPostContent());
        post.setPostRating(dto.getPostRating());

        if (dto.getPostShowNo() != null) {
            Show show = em.createQuery("SELECT s FROM Show s WHERE s.showNo = :no", Show.class)
                    .setParameter("no", dto.getPostShowNo())
                    .getSingleResult();
            post.setShow(show);
        }

        postRepository.save(post);
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void deletePost(int postNo, Long memberId, boolean devMode) {
        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        if (!devMode && !post.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    /**
     * 전체 게시글 조회
     */
    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "postRegisterDate")).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 게시글 상세 조회
     */
    public PostResponseDto getPostById(int postNo) {
        Post post = postRepository.findById(postNo).orElseThrow();
        return toDto(post);
    }

    /**
     * 공연 상태별 게시글 조회
     */
    public List<PostResponseDto> getPostsByShowState(int state) {
        return postRepository.findByShow_ShowState(state, Sort.by(Sort.Direction.DESC, "postRegisterDate")).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Post → PostResponseDto 변환
     */
    private PostResponseDto toDto(Post post) {
        String showName = null;
        try {
            if (post.getShow() != null && post.getShow().getShowInfo() != null) {
                showName = post.getShow().getShowInfo().getName();
            }
        } catch (Exception e) {
            System.out.println("❗ 공연 이름 가져오기 실패: " + e.getMessage());
        }

        return new PostResponseDto(
                post.getPostNo(),
                post.getPostTitle(),
                post.getPostContent(),
                post.getPostRating(),
                post.getNickname(),
                post.getPostRegisterDate() != null ? post.getPostRegisterDate().toString() : null,
                showName,
                post.getMember().getId()  // member_no → member.getId()
        );
    }
}
