package dding.board.like.dto.Response;


import dding.board.like.entity.ArticleLike;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ArticleLikeResponse {

    private Long articleLikeId;
    private Long articleId;
    private Long user_id;
    private LocalDateTime createdAt;

    public static ArticleLikeResponse from (ArticleLike articleLike)
    {
        ArticleLikeResponse response = new ArticleLikeResponse();
        response.articleLikeId = articleLike.getArticleLikeId();
        response.articleId = articleLike.getArticleId();
        response.user_id = articleLike.getUser_id();
        response.createdAt = articleLike.getCreatedAt();
        return response;
    }



}
