package com.newsfeed.board.like.controller;
// 오세창

import com.newsfeed.board.common.dto.ApiResponseDto;
import com.newsfeed.board.common.security.UserDetailsImpl;
import com.newsfeed.board.like.dto.LikeDto;
import com.newsfeed.board.like.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;
    @PostMapping("/post/{id}/like")
    public ResponseEntity<ApiResponseDto> insert(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        return likeService.insert(id, userDetails);
    }
}
