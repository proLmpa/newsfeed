package com.newsfeed.board.post.controller;

import com.newsfeed.board.common.dto.ApiResponseDto;
import com.newsfeed.board.common.security.UserDetailsImpl;
import com.newsfeed.board.post.dto.PostRequestDto;
import com.newsfeed.board.post.dto.PostResponseDto;
import com.newsfeed.board.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post/list") //게시판 첫 화면 목록
    public String getList(Model m) {

        try {
            List<PostResponseDto> postList = postService.getPosts();
            m.addAttribute("postList", postList);
            return "postList";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/post/{id}") //선택한 게시물 조회
    public String getPostById(@PathVariable Long id,Model model) {
        PostResponseDto postList =  postService.getPost(id);
        model.addAttribute("postList",postList);
        return "post";

    }

    @GetMapping("/post") //글쓰기 화면으로 이동
    public String write() {
        return "writePost";
    }

    @PostMapping("/post") //글 작성
    public String writePost(PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails,Model model) {
        PostResponseDto responseDto = postService.createPost(requestDto, userDetails.getUser());
        model.addAttribute("post",responseDto);
        model.addAttribute("user",userDetails.getUser());

        return "redirect:/api/post/list";
    }



//     게시글 작성하기 (요구사항.2)
//    @PostMapping("/post")
//    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        // UserDetails.getUser() : Authentication의 Principle
//        return postService.createPost(requestDto, userDetails.getUser());
//    }
//
//    // 전체 게시글 작성 날짜 기준 내림차순으로 조회하기 (요구사항.1)
//    @GetMapping("/post")
//    public List<PostResponseDto> getPosts() {
//        return postService.getPosts();
//    }
//
//    // 선택한 게시글 조회하기 (요구사항.3)
//    @GetMapping("/post/{id}")
//    public ResponseEntity<?> getPost(@PathVariable Long id) {
//        try {
//            return ResponseEntity.ok().body(postService.getPost(id));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(400L, e.getMessage()));
//        }
//    }

    // 선택한 게시글 수정하기 (요구사항.4)
    @PutMapping("/post/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            return ResponseEntity.ok().body(postService.updatePost(id, requestDto, userDetails.getUser()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(400L, e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(400L, e.getMessage()));
        }
    }

    @DeleteMapping("/post/{id}")
    public String deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            postService.deletePost(id, userDetails.getUser());
            return "redirect:/api/post/list";
        } catch (Exception e) {
            return "writePost";
        }

    }



    // 선택한 게시글 삭제하기 (요구사항.5)
//    @DeleteMapping("/post/{id}")
//    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        try {
//            postService.deletePost(id, userDetails.getUser());
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(new ApiResponseDto(400L, e.getMessage()));
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(400L, e.getMessage()));
//        }
//
//        return ResponseEntity.ok().body(new ApiResponseDto(200L, "SUCCESS_DELETE_POST"));
//    }






}
