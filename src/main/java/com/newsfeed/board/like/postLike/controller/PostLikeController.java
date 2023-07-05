package com.newsfeed.board.like.postLike.controller;
// 오세창

import com.newsfeed.board.common.dto.ApiResponseDto;
import com.newsfeed.board.common.security.UserDetailsImpl;
import com.newsfeed.board.like.postLike.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostLikeController {

    private final PostLikeService postLikeService;
    @PostMapping("/post/{id}/like")
    public ResponseEntity<ApiResponseDto> insert(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        return postLikeService.insert(id, userDetails);
    }

    @DeleteMapping("/post/{id}/like")
    public ResponseEntity<ApiResponseDto> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        return postLikeService.delete(id, userDetails);
    }
}
