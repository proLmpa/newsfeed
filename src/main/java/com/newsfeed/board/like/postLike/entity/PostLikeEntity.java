package com.newsfeed.board.like.postLike.entity;
// 오세창
import com.newsfeed.board.post.entity.PostEntity;
import com.newsfeed.board.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "post_like_table")
public class PostLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity postEntity;


    public PostLikeEntity(UserEntity userEntity, PostEntity postEntity) {
        this.userEntity = userEntity;
        this.postEntity = postEntity;
    }
}
