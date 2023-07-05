package com.newsfeed.board.like.postLike.service;

import com.newsfeed.board.common.dto.ApiResponseDto;
import com.newsfeed.board.common.security.UserDetailsImpl;
import com.newsfeed.board.like.postLike.entity.PostLikeEntity;
import com.newsfeed.board.like.postLike.repository.PostLikeRepository;
import com.newsfeed.board.post.entity.PostEntity;
import com.newsfeed.board.post.repository.PostRepository;
import com.newsfeed.board.user.entity.UserEntity;
import com.newsfeed.board.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public ResponseEntity<ApiResponseDto> insert(Long id, UserDetailsImpl userDetails) throws Exception {

        UserEntity user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Could not found member id : " + userDetails.getId()));

        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not found member id : " + id));

        try {
            if (postLikeRepository.findByUserEntityAndPostEntity(user, post).isPresent()) {
                // 이미 좋아요 되어있으면 에러 반환
                throw new Exception("\"Like\" already exists.");
            } else {
                // Post의 likes가 1증가
                post.countLike();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDto(400L, "LIKE_ALREADY_EXISTS"));
        }

        PostLikeEntity like = new PostLikeEntity(user, post);

        postLikeRepository.save(like);

        return ResponseEntity.ok().body(new ApiResponseDto(200L, "SUCCESS_REGISTER_LIKE_IN_POST"));

    }

    @Transactional
    public ResponseEntity<ApiResponseDto> delete(Long id, UserDetailsImpl userDetails) throws Exception {

        UserEntity user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Could not found member id : " + userDetails.getId()));

        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not found post id : " + id));

        try {
            // 좋아요가 존재하는지 확인 후 삭제, 존재하지 않으면 에러 반환
            PostLikeEntity like = postLikeRepository.findByUserEntityAndPostEntity(user, post)
                    .orElseThrow(() -> new RuntimeException("\"Like\" not exists."));
                post.discountLike();
                postLikeRepository.delete(like);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDto(400L, "LIKE_NOT_EXISTS"));
        }

        return ResponseEntity.ok().body(new ApiResponseDto(200L, "SUCCESS_DELETE_LIKE_IN_POST"));
    }
}


