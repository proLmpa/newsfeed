package com.newsfeed.board.post.service;

import com.newsfeed.board.comment.entity.CommentEntity;
import com.newsfeed.board.comment.repository.CommentRepository;
import com.newsfeed.board.post.dto.PostRequestDto;
import com.newsfeed.board.post.dto.PostResponseDto;
import com.newsfeed.board.post.entity.PostEntity;
import com.newsfeed.board.post.repository.PostRepository;
import com.newsfeed.board.user.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository){
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    // 게시글 작성하기
    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, UserEntity user) {
        PostEntity post = new PostEntity(requestDto, user);

        postRepository.save(post);

        return new PostResponseDto(post);
    }

    // 전체 게시글 조회하기
    @Transactional(readOnly = true)
    public List<PostResponseDto> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map((PostEntity post) ->
                new PostResponseDto(post, findCommentList(post.getId()))
        ).toList();
    }

    // 선택 게시글 조회하기
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {
        PostEntity post = findPost(id);
        List<CommentEntity> commentList = findCommentList(post.getId());

        return new PostResponseDto(post, commentList);
    }

    // 게시글 수정하기
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, UserEntity user) {
        PostEntity post = findPost(id);

        if(matchUser(post, user)) {
            post.update(requestDto);
            List<CommentEntity> commentList = findCommentList(post.getId());

            return new PostResponseDto(post, commentList);
        } else {
            throw new RuntimeException("UNAUTHORIZED_REQUEST");
        }
    }

    // 게시글 삭제하기
    @Transactional
    public void deletePost(Long id, UserEntity user) {
        PostEntity post = findPost(id);

        if(matchUser(post, user)) {
            postRepository.delete(post);
        } else {
            throw new RuntimeException("UNAUTHORIZED_REQUEST");
        }
    }

    public PostEntity findPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("No such post exists"));
    }

    public List<CommentEntity> findCommentList(Long postId){
        return commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);
    }

    public boolean matchUser(PostEntity post, UserEntity user) {
        return post.getUser().getId().equals(user.getId());
    }
}
