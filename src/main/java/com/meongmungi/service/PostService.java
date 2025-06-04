package com.meongmungi.service;

import com.meongmungi.dto.PostRequestDto;
import com.meongmungi.dto.PostResponseDto;
import com.meongmungi.entity.CategoryType;
import com.meongmungi.entity.Post;
import com.meongmungi.entity.PostImage;
import com.meongmungi.entity.User;
import com.meongmungi.repository.PostImageRepository;
import com.meongmungi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    /**
     * 게시글 생성
     */
    @Transactional
    public PostResponseDto createPost(User owner, PostRequestDto dto) {
        // 1) Post 엔티티 생성
        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .category(dto.getCategory())
                .owner(owner)
                .build();

        // 2) 저장 (ID 생성)
        Post savedPost = postRepository.save(post);

        // 3) 이미지 URL이 있으면 PostImage 엔티티로 저장
        if (dto.getImageUrls() != null && !dto.getImageUrls().isEmpty()) {
            List<PostImage> imageEntities = dto.getImageUrls().stream()
                    .map(url -> PostImage.builder()
                            .imageUrl(url)
                            .post(savedPost)
                            .build())
                    .collect(Collectors.toList());
            postImageRepository.saveAll(imageEntities);
            savedPost.getImages().addAll(imageEntities);
        }

        // 4) 응답 DTO로 변환
        return mapToDto(savedPost);
    }

    /**
     * 카테고리별 게시글 리스트 조회
     */
    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPosts(CategoryType category) {
        List<Post> posts;
        if (category == null) {
            posts = postRepository.findAll();
        } else {
            posts = postRepository.findAllByCategory(category);
        }

        return posts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * 단일 게시글 조회
     */
    @Transactional(readOnly = true)
    public PostResponseDto getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));
        return mapToDto(post);
    }

    /**
     * 게시글 수정 (작성자만 가능)
     */
    @Transactional
    public PostResponseDto updatePost(User owner, Long postId, PostRequestDto dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));

        if (!post.getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("게시글 수정 권한이 없습니다.");
        }

        // 1) 기본 필드 업데이트
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCategory(dto.getCategory());

        // 2) 이미지 업데이트: 기존 이미지 전부 삭제 후, 새로 등록
        post.getImages().clear();
        postImageRepository.deleteAllById(
                post.getImages().stream().map(PostImage::getId).collect(Collectors.toList())
        );

        if (dto.getImageUrls() != null && !dto.getImageUrls().isEmpty()) {
            List<PostImage> newImages = dto.getImageUrls().stream()
                    .map(url -> PostImage.builder()
                            .imageUrl(url)
                            .post(post)
                            .build())
                    .collect(Collectors.toList());
            postImageRepository.saveAll(newImages);
            post.getImages().addAll(newImages);
        }

        // 3) 저장된 엔티티를 통해 응답 DTO 생성
        return mapToDto(post);
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void deletePost(User owner, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));

        if (!post.getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("게시글 삭제 권한이 없습니다.");
        }
        postRepository.delete(post);
    }

    /**
     * Post 엔티티
     */
    private PostResponseDto mapToDto(Post post) {
        // 이미지 URL만 따로 뽑아서 전달
        List<String> imageUrls = post.getImages().stream()
                .map(PostImage::getImageUrl)
                .collect(Collectors.toList());

        // 댓글 개수 & 좋아요 개수
        long commentCount = post.getComments().size();
        long likeCount = post.getLikes().size();

        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .ownerId(post.getOwner().getId())
                .ownerNickname(post.getOwner().getNickname())
                .commentCount(commentCount)
                .likeCount(likeCount)
                .imageUrls(imageUrls)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
