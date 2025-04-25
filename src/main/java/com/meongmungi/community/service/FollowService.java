package com.meongmungi.community.service;

import com.meongmungi.community.domain.Follow;
import com.meongmungi.community.dto.FollowDto;
import com.meongmungi.community.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;

    @Transactional
    public FollowDto.Response followUser(FollowDto.Request request) {
        if (request.getFollowerId().equals(request.getFollowingId())) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }

        Optional<Follow> existingFollow = followRepository.findByFollowerIdAndFollowingId(
                request.getFollowerId(), request.getFollowingId());

        if (existingFollow.isPresent()) {
            return FollowDto.Response.fromEntity(existingFollow.get());
        }

        Follow follow = Follow.builder()
                .followerId(request.getFollowerId())
                .followingId(request.getFollowingId())
                .build();

        Follow savedFollow = followRepository.save(follow);
        return FollowDto.Response.fromEntity(savedFollow);
    }

    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        if (!followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new IllegalArgumentException("팔로우 관계가 존재하지 않습니다.");
        }

        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }

    public boolean checkIfUserFollows(Long followerId, Long followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    public List<FollowDto.Response> getFollowings(Long userId) {
        List<Follow> followings = followRepository.findByFollowerId(userId);

        return followings.stream()
                .map(FollowDto.Response::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FollowDto.Response> getFollowers(Long userId) {
        List<Follow> followers = followRepository.findByFollowingId(userId);

        return followers.stream()
                .map(FollowDto.Response::fromEntity)
                .collect(Collectors.toList());
    }

    public int getFollowingCount(Long userId) {
        return followRepository.countByFollowerId(userId);
    }

    public int getFollowerCount(Long userId) {
        return followRepository.countByFollowingId(userId);
    }
}