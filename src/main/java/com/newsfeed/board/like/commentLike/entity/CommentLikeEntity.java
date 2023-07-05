package com.newsfeed.board.like.commentLike.entity;

import com.newsfeed.board.comment.entity.CommentEntity;
import com.newsfeed.board.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comment_like_table")
public class CommentLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "comment_id")
    private CommentEntity comment;

    private CommentLikeEntity (UserEntity user, CommentEntity comment) {
        this.user = user;
        this.comment = comment;
    }
}
