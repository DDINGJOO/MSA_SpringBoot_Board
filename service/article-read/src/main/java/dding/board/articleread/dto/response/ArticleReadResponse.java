package dding.board.articleread.dto.response;


import dding.board.articleread.repository.ArticleQueryModel;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ArticleReadResponse {
    private Long articleId;
    private String title;
    private String content;
    private Long boardId;
    private Long writerId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long articleCommentCount;
    private Long articleLikeCount;
    private Long articleViewCount;


    public static ArticleReadResponse from(ArticleQueryModel articleQueryModel , Long viewCount)
    {
        ArticleReadResponse response = new ArticleReadResponse();
        response.articleCommentCount = articleQueryModel.getArticleCommentCount();
        response.articleId = articleQueryModel.getArticleId();
        response.articleViewCount = viewCount;
        response.articleLikeCount = articleQueryModel.getArticleLikeCount();
        response.boardId = articleQueryModel.getBoardId();
        response.content = articleQueryModel.getTitle();
        response.title  =articleQueryModel.getTitle();
        response.createdAt =articleQueryModel.getCreatedAt();
        response.modifiedAt = articleQueryModel.getModifiedAt();
        response.writerId = articleQueryModel.getWriterId();

        return response;
    }
}
