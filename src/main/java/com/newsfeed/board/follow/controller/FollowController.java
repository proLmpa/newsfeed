package com.newsfeed.board.follow.controller;

import com.newsfeed.board.common.dto.ApiResponseDto;
import com.newsfeed.board.common.security.UserDetailsImpl;
import com.newsfeed.board.follow.service.FollowService;
import com.newsfeed.board.post.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FollowController {

    private final FollowService followService;

    // 팔로우 메서드
    @PostMapping("/follow/{followingId}")
    public ResponseEntity<ApiResponseDto> following (@PathVariable String followingId, @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        return followService.follow(followingId, userDetails);
    }

    // 언팔로우 메서드
    @PostMapping("/unfollow/{followingId}")
    public ResponseEntity<ApiResponseDto> unfollowing (@PathVariable String followingId, @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        return followService.unfollow(followingId, userDetails);
    }

    // 팔로우 사람의 게시글 조회
    @GetMapping("/follow/{followingId}/post")
    public List<PostResponseDto> readFollowerPost (@PathVariable String followingId, @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        return followService.readFollowerPost(followingId, userDetails);
    }
}
