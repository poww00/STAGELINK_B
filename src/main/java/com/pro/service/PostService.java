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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager em;

    public PostService(PostRepository postRepository, MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void savePost(PostRequestDto dto, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();

        Show show = em.createQuery("SELECT s FROM Show s WHERE s.showNo = :no", Show.class)
                .setParameter("no", dto.getPostShowNo())
                .getSingleResult();

        Post post = new Post();
        post.setMember(member);
        post.setNickname(member.getNickname()); // 복사 저장
        post.setShow(show);
        post.setPostTitle(dto.getPostTitle());
        post.setPostContent(dto.getPostContent());
        post.setPostRating(dto.getPostRating());
        post.setPostRegisterDate(LocalDateTime.now());
        post.setPostReportCount(0);

        postRepository.save(post);
    }

    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PostResponseDto getPostById(int postNo) {
        Post post = postRepository.findById(postNo).orElseThrow();
        return toDto(post);
    }

    public List<PostResponseDto> getPostsByShowState(int state) {
        return postRepository.findByShow_ShowState(state).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

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
                showName
        );
    }
}
