package com.newsfeed.board.post.entity;

import com.newsfeed.board.comment.entity.CommentEntity;
import com.newsfeed.board.common.entity.TimeStamped;
import com.newsfeed.board.post.dto.PostRequestDto;
import com.newsfeed.board.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
@Table(name = "post")
public class PostEntity extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contents", nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "comment_list")
    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<CommentEntity> commentList;

    @ColumnDefault("0")
    @Column(name = "likes", nullable = false)
    private Integer likes;

    public void addCommentList(CommentEntity comment) {
        this.commentList.add(comment);
        comment.setPost(this);
    }

    public PostEntity(PostRequestDto requestDto, UserEntity user) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.user = user;
        this.commentList = new ArrayList<>();
    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();
    }

    public void countLike() {
        this.likes++;
    }

    public void discountLike() {
        this.likes--;
    }
}
