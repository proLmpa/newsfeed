package com.newsfeed.board.follow.service;

import com.newsfeed.board.common.dto.ApiResponseDto;
import com.newsfeed.board.common.security.UserDetailsImpl;
import com.newsfeed.board.follow.entity.FollowEntity;
import com.newsfeed.board.follow.repository.FollowRepository;
import com.newsfeed.board.post.dto.PostResponseDto;
import com.newsfeed.board.post.entity.PostEntity;
import com.newsfeed.board.post.repository.PostRepository;
import com.newsfeed.board.user.entity.UserEntity;
import com.newsfeed.board.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j(topic = "FollowService Log")
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;

    // 팔로우 메서드 // @UniqueConstraints 애너테이션 알아보기
    @Transactional
    public ResponseEntity<ApiResponseDto> follow(String followingUserId, @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {

        UserEntity followingUser = findFollowingUser(followingUserId); // 팔로우를 할 대상의 ID
        UserEntity followerUser = userDetails.getUser(); // 팔로우를 하는 사람의 ID (본인 ID) Long id

        try {
            if (followingUser.getId().equals(userDetails.getId())) {
                throw new Exception("CANNOT_FOLLOW_YOURSELF");
            } else {
                FollowEntity followEntity = new FollowEntity(followingUser, followerUser);
                followRepository.save(followEntity);
                return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "SUCCESS_FOLLOW_USER"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }


    // 언팔로우
    @Transactional
    public ResponseEntity<ApiResponseDto> unfollow(String followingUserId, @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {

        UserEntity followingUser = findFollowingUser(followingUserId); // 팔로우를 할 대상의 ID
        UserEntity followerUser = userDetails.getUser(); // 팔로우를 하는 사람의 ID (본인 ID)
        Optional<FollowEntity> followEntityOptional = followRepository.findByFollowerUserAndFollowingUser(followerUser, followingUser);

        try {
            if (followEntityOptional.isPresent()) { // followEntityOptional 에 값이 있는지 확인, true -> 팔로우 관계 / false -> 팔로우 관계 x
                FollowEntity followEntity = followEntityOptional.get();
                followRepository.delete(followEntity);
            } else {
                throw new Exception("USER_NOT_FOLLOWING");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }

        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "SUCCESS_UNFOLLOW_USER"));

    }

    // 팔로잉 유저의 게시글 조회
    public List<PostResponseDto> readFollowerPost(String followingUserId, @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {

        UserEntity followingUser = findFollowingUser(followingUserId);
        UserEntity followerUser = userDetails.getUser();
        Optional<FollowEntity> followEntityOptional = followRepository.findByFollowerUserAndFollowingUser(followerUser, followingUser);

        if (followEntityOptional.isEmpty()) {
            throw new Exception("USER_NOT_FOLLOWING");
        }

        List<PostEntity> postList = postRepository.findByUser(followingUser);
        return postList.stream().map(PostResponseDto::new).toList();
    }


    public UserEntity findFollowingUser(String id) throws Exception {
        return userRepository.findById(id)
                .orElseThrow(() -> new Exception("No Such User Exists"));
    }

    // 하나로 합쳐보기
}
