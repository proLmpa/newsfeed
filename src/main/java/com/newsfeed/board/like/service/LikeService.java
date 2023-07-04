package com.newsfeed.board.like.service;

import com.newsfeed.board.like.dto.LikeDto;
import com.newsfeed.board.like.entity.LikeEntity;
import com.newsfeed.board.like.repository.LikeRepository;
import com.newsfeed.board.post.entity.PostEntity;
import com.newsfeed.board.post.repository.PostRepository;
import com.newsfeed.board.user.entity.UserEntity;
import com.newsfeed.board.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public void insert(LikeDto likeDto) throws Exception {

        UserEntity user = userRepository.findById(likeDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Could not found member id : " + likeDto.getUserId()));

        PostEntity post = postRepository.findById(likeDto.getPostId())
                .orElseThrow(() -> new RuntimeException("Could not found member id : " + likeDto.getUserId()));

        // 이미 좋아요 되어있으면 에러 반환
        if (likeRepository.findUserAndPost(user, post).isPresent()) {
            throw new Exception();
        }

        LikeEntity like = new LikeEntity(user, post);

        likeRepository.save(like);

    }

}


