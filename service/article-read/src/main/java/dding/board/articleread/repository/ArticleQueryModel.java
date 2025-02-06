package dding.board.articleread.repository;


import dding.board.articleread.client.ArticleClient;
import dding.board.board.event.payload.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleQueryModel {
    private Long articleId;
    private String title;
    private String content;
    private Long boardId;
    private Long writerId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long articleCommentCount;
    private Long articleLikeCount;


    public static ArticleQueryModel create(ArticleCreatedEventPayload payload)
    {
        ArticleQueryModel articleQueryModel = new ArticleQueryModel();
        articleQueryModel.articleCommentCount = 0L;
        articleQueryModel.articleLikeCount = 0L;
        articleQueryModel.createdAt = payload.getCreatedAt();
        articleQueryModel.writerId = payload.getWriterId();
        articleQueryModel.articleId = payload.getArticleId();
        articleQueryModel.boardId =payload.getBoardId();
        articleQueryModel.modifiedAt = payload.getModifiedAt();
        articleQueryModel.content = payload.getContent();
        articleQueryModel.title = payload.getTitle();
        return articleQueryModel;
    }

    public static ArticleQueryModel create(ArticleClient.ArticleResponse article, Long commentCount, Long likeCount)
    {
        ArticleQueryModel articleQueryModel = new ArticleQueryModel();
        articleQueryModel.articleCommentCount = commentCount;
        articleQueryModel.articleLikeCount = likeCount;
        articleQueryModel.createdAt = article.getCreatedAt();
        articleQueryModel.writerId = article.getWriterId();
        articleQueryModel.articleId = article.getArticleId();
        articleQueryModel.boardId =article.getBoardId();
        articleQueryModel.modifiedAt = article.getModifiedAt();
        articleQueryModel.content = article.getContent();
        articleQueryModel.title = article.getTitle();
        return articleQueryModel;
    }

    public void updateBy(CommentCreatedEventPayload payload)
    {
        this.articleCommentCount = payload.getArticleCommentCount();
    }
    public void updateBy(CommentDeletedEventPayload payload)
    {
        this.articleCommentCount = payload.getArticleCommentCount();
    }
    public void updateBy(ArticleLikedEventPayload payload)
    {
        this.articleCommentCount = payload.getArticleLikeCount();
    }
    public void updateBy(ArticleUnlikedEventPayload payload)
    {
        this.articleCommentCount = payload.getArticleLikeCount();
    }

    public void updateBy(ArticleUpdatedEventPayload payload)
    {
        this.title =payload.getTitle();
        this.boardId = payload.getBoardId();
        this.content = payload.getContent();
        this.writerId = payload.getWriterId();
        this.modifiedAt = payload.getModifiedAt();
        this.createdAt = payload.getCreatedAt();

    }
}
