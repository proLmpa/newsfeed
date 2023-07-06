package com.newsfeed.board.comment.controller;

import com.newsfeed.board.comment.dto.CommentRequestDto;
import com.newsfeed.board.comment.dto.CommentResponseDto;
import com.newsfeed.board.comment.service.CommentService;
import com.newsfeed.board.common.dto.ApiResponseDto;
import com.newsfeed.board.common.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) { this.commentService = commentService; }


    @GetMapping("/comment") //댓글 가져오기
    public List<CommentResponseDto> getComments(@RequestParam Long postId){
            return commentService.getComments(postId);
    }

    @PostMapping("/comment")
    public ResponseEntity<?> createComment(@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            return ResponseEntity.ok().body(commentService.createComment(requestDto, userDetails.getUser()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(400L, e.getMessage()));
        }
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<?> updateComment(@PathVariable("id") Long id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            return ResponseEntity.ok().body(commentService.updateComment(id, requestDto, userDetails.getUser()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(400L, e.getMessage()));
        }
    }

    @DeleteMapping("/comment/{id}")
    public ApiResponseDto deleteComment(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            commentService.deleteComment(id, userDetails.getUser());
        } catch (RuntimeException e){
            return new ApiResponseDto(400L, e.getMessage());
        }

        return new ApiResponseDto(200L, "SUCCESS_DELETE_COMMENT");
    }
}
