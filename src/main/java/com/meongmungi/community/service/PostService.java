package com.meongmungi.community.service;

import com.meongmungi.community.domain.Post;
import com.meongmungi.community.dto.CommentDto;
import com.meongmungi.community.dto.PostDto;
import com.meongmungi.community.repository.CommentRepository;
import com.meongmungi.community.repository.LikeRepository;
import com.meongmungi.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public PostDto.Response createPost(PostDto.Request request) {
        Post post = Post.builder()
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .userId(request.getUserId())
                .userName(request.getUserName())
                .userProfileImage(request.getUserProfileImage())
                .build();

        Post savedPost = postRepository.save(post);
        return PostDto.Response.fromEntity(savedPost, false);
    }

    public PostDto.Response getPostById(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        boolean isLiked = false;
        if (userId != null) {
            isLiked = likeRepository.existsByPostIdAndUserId(postId, userId);
        }

        return PostDto.Response.fromEntity(post, isLiked);
    }

    public PostDto.DetailResponse getPostWithComments(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        boolean isLiked = false;
        if (userId != null) {
            isLiked = likeRepository.existsByPostIdAndUserId(postId, userId);
        }

        List<CommentDto.Response> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId).stream()
                .map(CommentDto.Response::fromEntity)
                .collect(Collectors.toList());

        return PostDto.DetailResponse.fromEntity(post, isLiked, comments);
    }

    @Transactional
    public PostDto.Response updatePost(Long postId, PostDto.Request request, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (!post.getUserId().equals(userId)) {
            throw new IllegalArgumentException("게시글 작성자만 수정할 수 있습니다.");
        }

        Post updatedPost = Post.builder()
                .id(post.getId())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .userId(post.getUserId())
                .userName(post.getUserName())
                .userProfileImage(post.getUserProfileImage())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();

        Post savedPost = postRepository.save(updatedPost);

        boolean isLiked = likeRepository.existsByPostIdAndUserId(postId, userId);

        return PostDto.Response.fromEntity(savedPost, isLiked);
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (!post.getUserId().equals(userId)) {
            throw new IllegalArgumentException("게시글 작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    public Page<PostDto.Response> getAllPosts(Pageable pageable, Long userId) {
        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        return posts.map(post -> {
            boolean isLiked = false;
            if (userId != null) {
                isLiked = likeRepository.existsByPostIdAndUserId(post.getId(), userId);
            }
            return PostDto.Response.fromEntity(post, isLiked);
        });
    }

    public Page<PostDto.Response> getUserPosts(Long userId, Pageable pageable) {
        Page<Post> posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        return posts.map(post -> {
            boolean isLiked = likeRepository.existsByPostIdAndUserId(post.getId(), userId);
            return PostDto.Response.fromEntity(post, isLiked);
        });
    }

    public Page<PostDto.Response> getFollowingPosts(Long userId, Pageable pageable) {
        Page<Post> posts = postRepository.findFollowingPosts(userId, pageable);

        return posts.map(post -> {
            boolean isLiked = likeRepository.existsByPostIdAndUserId(post.getId(), userId);
            return PostDto.Response.fromEntity(post, isLiked);
        });
    }
}