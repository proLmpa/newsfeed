package com.newsfeed.board.comment.entity;

import com.newsfeed.board.comment.dto.CommentRequestDto;
import com.newsfeed.board.common.entity.TimeStamped;
import com.newsfeed.board.post.entity.PostEntity;
import com.newsfeed.board.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comment")
public class CommentEntity extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column(name = "content", nullable = false)
    private String content;

    public CommentEntity(CommentRequestDto requestDto, UserEntity user, PostEntity post) {
        this.content = requestDto.getContents();
        this.user = user;
        this.post = post;
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContents();
    }
}
