package dding.board.like.entity;


import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "article_like")
@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
public class ArticleLike {
    @Id
    private Long articleLikeId;
    private Long articleId;
    private Long user_id;
    private LocalDateTime createdAt;


    public  static ArticleLike from (Long articleLikeId, Long articleId, Long user_id)
    {
        ArticleLike articleLike = new ArticleLike();
        articleLike.articleLikeId = articleLikeId;
        articleLike.articleId  =articleId;
        articleLike.user_id = user_id;
        articleLike.createdAt = LocalDateTime.now();
        return  articleLike;
    }
}
