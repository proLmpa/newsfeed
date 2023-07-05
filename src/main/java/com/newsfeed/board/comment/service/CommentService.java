package com.newsfeed.board.comment.service;

import com.newsfeed.board.comment.dto.CommentRequestDto;
import com.newsfeed.board.comment.dto.CommentResponseDto;
import com.newsfeed.board.comment.entity.CommentEntity;
import com.newsfeed.board.comment.repository.CommentRepository;
import com.newsfeed.board.post.dto.PostResponseDto;
import com.newsfeed.board.post.entity.PostEntity;
import com.newsfeed.board.post.repository.PostRepository;
import com.newsfeed.board.user.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto, UserEntity user) {
        // 선택한 게시글의 DB 존재 여부 확인
        PostEntity post = findPost(requestDto.getPostId());

        // 댓글 등록 후 등록된 댓글 반환하기
        CommentEntity comment = new CommentEntity(requestDto, user, post);
        post.addCommentList(comment);

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, UserEntity user) {
        // 선택한 댓글이 DB이 존재하는지 확인
        CommentEntity comment = findComment(commentId);

        // 해당 사용자가 작성한 댓글 여부 혹은 관리자 여부 확인
        if (matchUser(comment, user)) {
            comment.update(requestDto);

            return new CommentResponseDto(comment);
        } else {
            throw new RuntimeException("UNAUTHORIZED_REQUEST");
        }
    }

    @Transactional
    public void deleteComment(Long commentId, UserEntity user) {
        // 선택한 댓글이 DB이 존재하는지 확인
        CommentEntity comment = findComment(commentId);

        // 해당 사용자가 작성한 댓글 여부 혹은 관리자 여부 확인
        if (matchUser(comment, user)) {
            commentRepository.delete(comment);

        } else {
            throw new RuntimeException("UNAUTHORIZED_REQUEST");
        }
    }

    @Transactional(readOnly = true)
    public PostEntity findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new RuntimeException("No such post exists")
        );
    }

    @Transactional(readOnly = true)
    public CommentEntity findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new RuntimeException("No such comment exists")
        );
    }

    public boolean matchUser(CommentEntity comment, UserEntity user) {
        return comment.getUser().getId().equals(user.getId());
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(Long id) {
        return commentRepository.findAllByPost_Id(id).stream().map(CommentResponseDto::new).toList();

        //select * from comment where bno = bno


    }


}
