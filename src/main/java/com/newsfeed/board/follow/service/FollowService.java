package com.newsfeed.board.follow.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.board.common.dto.ApiResponseDto;
import com.newsfeed.board.common.security.UserDetailsImpl;
import com.newsfeed.board.follow.entity.FollowEntity;
import com.newsfeed.board.follow.repository.FollowRepository;
import com.newsfeed.board.post.dto.PostResponseDto;
import com.newsfeed.board.post.entity.PostEntity;
import com.newsfeed.board.post.repository.PostRepository;
import com.newsfeed.board.user.entity.UserEntity;
import com.newsfeed.board.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
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
        Optional<FollowEntity> followEntityOptional = followRepository.findByFollowerUserAndFollowingUser(followerUser, followingUser);

        try {
            if (followEntityOptional.isPresent()) { // followEntityOptional 에 값이 있는지 확인, true -> 팔로우 관계 / false -> 팔로우 관계 x
                log.info("이미 팔로우가 되어있습니다.");
                throw new Exception();
            } else if (followingUser.getId().equals(userDetails.getId())) {
                log.info("본인은 팔로우 할 수 없습니다.");
                throw new Exception();
            } else {
                FollowEntity followEntity = new FollowEntity(followingUser, followerUser);
                followRepository.save(followEntity);
                log.info("팔로우 성공");
                return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "팔로우 성공."));
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "팔로우 실패"));
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
                log.info("언팔로우를 하였습니다.");
            } else {
                throw new Exception("팔로우 관계가 아닙니다.");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "언팔로우 실패"));
        }

        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "언팔로우 성공"));

    }

    // 팔로잉 유저의 게시글 조회
    public List<PostResponseDto> readFollowerPost(String followingUserId, @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {

        UserEntity followingUser = findFollowingUser(followingUserId);
        UserEntity followerUser = userDetails.getUser();
        Optional<FollowEntity> followEntityOptional = followRepository.findByFollowerUserAndFollowingUser(followerUser, followingUser);

        if (followEntityOptional.isEmpty()) {
            throw new Exception("팔로우 관계가 아닙니다.");
        }

        List<PostEntity> postList = postRepository.findByUser(followingUser);
        return postList.stream().map(PostResponseDto::new).toList();
    }


    public UserEntity findFollowingUser(String id) throws Exception {
        return userRepository.findById(id)
                .orElseThrow(() -> new Exception("해당 유저는 없습니다."));
    }

    // 하나로 합쳐보기
}
