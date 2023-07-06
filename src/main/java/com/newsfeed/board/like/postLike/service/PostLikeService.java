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

        UserEntity user = findUserById(userDetails);
        PostEntity post = findPostById(id);

        // 본인 게시글에는 좋아요 불가

        try {
            if (postLikeRepository.findByUserEntityAndPostEntity(user, post).isPresent()) { // 이미 좋아요 되어있으면 에러 반환
                throw new Exception("\"Like\" already exists.");
            } else if (post.getUser().getId().equals(userDetails.getId())) { // 댓글 작성자의 id 와 현재 인가된 유저의 id 를 비교하요 같으면 에러 반환
                throw new Exception("Same person cannot \"Like\"");
            } else {
                post.countLike(); // post의 likes가 1 증
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDto(400L, "LIKE_ERROR"));
        }

        PostLikeEntity like = new PostLikeEntity(user, post); // 어떤 유저와 게시글에 좋아요 달았는지 관계 설정

        postLikeRepository.save(like); // DB 저장
        return ResponseEntity.ok().body(new ApiResponseDto(200L, "SUCCESS_REGISTER_LIKE_IN_POST"));

    }

    @Transactional
    public ResponseEntity<ApiResponseDto> delete(Long id, UserDetailsImpl userDetails) throws Exception {

        UserEntity user = findUserById(userDetails);
        PostEntity post = findPostById(id);

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

    public UserEntity findUserById(UserDetailsImpl userDetails) {
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Could not found member id : " + userDetails.getId()));
    }

    public PostEntity findPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not found post id : " + id));
    }
}


