package com.newsfeed.board.like.commentLike.service;


import com.newsfeed.board.comment.entity.CommentEntity;
import com.newsfeed.board.comment.repository.CommentRepository;
import com.newsfeed.board.common.dto.ApiResponseDto;
import com.newsfeed.board.common.security.UserDetailsImpl;
import com.newsfeed.board.like.commentLike.entity.CommentLikeEntity;
import com.newsfeed.board.like.commentLike.repository.CommentLikeRepository;
import com.newsfeed.board.user.entity.UserEntity;
import com.newsfeed.board.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ResponseEntity<ApiResponseDto> insert(Long id, UserDetailsImpl userDetails) throws Exception {

        // 유저, 댓글 존재하는지 확인
        UserEntity user = findUserById(userDetails);
        CommentEntity comment = findCommentById(id);

        try {
            if (commentLikeRepository.findByUserEntityAndCommentEntity(user, comment).isPresent()) {  // 이미 좋아요 되어있으면 에러 반환
                throw new Exception("\"Like\" already exists.");
            } else if (comment.getUser().getId().equals(userDetails.getId())) { // 댓글 작성자의 id 와 현재 인가된 유저의 id 를 비교하요 같으면 에러 반환
                throw new Exception("Same person cannot \"Like\"");
            }
            else {
                comment.countLike(); // comment의 likes가 1 증가
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDto(400L, "LIKE_ERROR"));
        }

        CommentLikeEntity like = new CommentLikeEntity(user, comment); // 어떤 유저와 댓글에 좋아요 달았는지 관계 설정

        commentLikeRepository.save(like); // DB 저장

        return ResponseEntity.ok().body(new ApiResponseDto(200L, "SUCCESS_REGISTER_LIKE_IN_COMMENT"));

    }

    @Transactional
    public ResponseEntity<ApiResponseDto> delete(Long id, UserDetailsImpl userDetails) throws Exception {

        UserEntity user = findUserById(userDetails);
        CommentEntity comment = findCommentById(id);

        try {
            // 좋아요가 존재하는지 확인 후 삭제, 존재하지 않으면 에러 반환
            CommentLikeEntity like = commentLikeRepository.findByUserEntityAndCommentEntity(user, comment)
                    .orElseThrow(() -> new RuntimeException("\"Like\" not exists."));
            comment.discountLike();
            commentLikeRepository.delete(like);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDto(400L, "LIKE_NOT_EXISTS"));
        }

        return ResponseEntity.ok().body(new ApiResponseDto(200L, "SUCCESS_DELETE_LIKE_IN_COMMENT"));
    }

    public UserEntity findUserById(UserDetailsImpl userDetails) {
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Could not found member id : " + userDetails.getId()));
    }

    public CommentEntity findCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not found member id : " + id));
    }
}
