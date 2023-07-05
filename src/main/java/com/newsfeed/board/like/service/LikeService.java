package com.newsfeed.board.like.service;

import com.newsfeed.board.common.dto.ApiResponseDto;
import com.newsfeed.board.common.security.UserDetailsImpl;
import com.newsfeed.board.like.dto.LikeDto;
import com.newsfeed.board.like.entity.LikeEntity;
import com.newsfeed.board.like.repository.LikeRepository;
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
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public ResponseEntity<ApiResponseDto> insert(Long id, UserDetailsImpl userDetails) throws Exception {

        UserEntity user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Could not found member id : " + userDetails.getId()));

        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not found member id : " + id));

        try {
            if (likeRepository.findByUserEntityAndPostEntity(user, post).isPresent()) {
                // 이미 좋아요 되어있으면 에러 반환
                throw new Exception("\"Like\" already exists.");
            } else {
                // Post의 likes가 1증가
                post.countLike();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok().body(new ApiResponseDto(409L, "LIKE_ALREADY_EXISTS"));
        }

        LikeEntity like = new LikeEntity(user, post);

        likeRepository.save(like);

        return ResponseEntity.ok().body(new ApiResponseDto(200L, "SUCCESS_REGISTER_LIKE_IN_POST"));

    }

}


