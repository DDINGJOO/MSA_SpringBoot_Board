package dding.board.article.service;


import dding.board.article.entity.Article;
import dding.board.article.entity.BoardArticleCount;
import dding.board.article.exceptions.NotFoundArticleById;
import dding.board.article.repository.ArticleRepository;
import dding.board.article.dto.request.ArticleCreateRequest;
import dding.board.article.dto.request.ArticleUpdateRequest;
import dding.board.article.dto.response.ArticlePageResponse;
import dding.board.article.dto.response.ArticleResponse;
import dding.board.article.repository.BoardArticleCountRepository;
import dding.board.common.PrimaryKeyProvider.PrimaryIdProvider;
import dding.board.common.event.EventType;
import dding.board.common.event.payload.ArticleCreatedEventPayload;
import dding.board.common.event.payload.ArticleUpdatedEventPayload;
import dding.board.common.outboxmessagerelay.OutboxEventPublisher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final PrimaryIdProvider primaryIdProvider = new PrimaryIdProvider();
    private final ArticleRepository articleRepository;
    private final BoardArticleCountRepository boardArticleCountRepository;
    private final OutboxEventPublisher outboxEventPublisher;


    @Transactional
    public ArticleResponse create(ArticleCreateRequest request)
    {
        Article article = articleRepository.save(
                Article.create(
                        primaryIdProvider.getId(), request.getTitle(), request.getContent(), request.getBoardId(), request.getWriterId())
        );

        int result = boardArticleCountRepository.increase(request.getBoardId());
        if(result == 0 )
        {
            boardArticleCountRepository.save(
                    BoardArticleCount.init(request.getBoardId())
            );
        }
        outboxEventPublisher.publish(
                EventType.ARTICLE_CREATED,
                ArticleCreatedEventPayload.builder()
                        .articleId(article.getArticleId())
                        .title(article.getTitle())
                        .content(article.getContent())
                        .boardId(article.getBoardId())
                        .writerId(article.getWriterId())
                        .createdAt(article.getCreatedAt())
                        .modifiedAt(article.getModifiedAt())
                        .boardArticleCount(count(article.getBoardId()))
                        .build(),
                article.getBoardId()//shardKey
        );
        return ArticleResponse.form(article);
    }



    @Transactional
    public ArticleResponse update(Long articleId,ArticleUpdateRequest req)
    {
       Article article = articleRepository.findById(articleId).orElseThrow();
       article.update(req.getTitle(), req.getContent());
        outboxEventPublisher.publish(
                EventType.ARTICLE_UPDATED,
                ArticleUpdatedEventPayload.builder()
                        .articleId(article.getArticleId())
                        .title(article.getTitle())
                        .content(article.getContent())
                        .boardId(article.getBoardId())
                        .writerId(article.getWriterId())
                        .createdAt(article.getCreatedAt())
                        .modifiedAt(article.getModifiedAt())
                        .build(),
                article.getBoardId()//shardKey
        );
       return ArticleResponse.form(article);
    }



    public ArticleResponse read(Long articleId)
    {
        return ArticleResponse.form(articleRepository.findById(articleId).orElseThrow());

    }


    public ArticlePageResponse readAll(Long boardId, Long page, Long pageSize)
    {
        return ArticlePageResponse.of(
                articleRepository.findAll(boardId, (page -1) * pageSize, pageSize).stream()
                        .map(ArticleResponse::form)
                        .toList(),
                articleRepository.count(
                        boardId,
                        PageLimitCalculator.calculator(page,pageSize,10L)
                )
        );
    }




    public List<ArticleResponse> readAllInfiniteScroll(Long boardId , Long pageSize, Long lastArticleId)
    {
        List<Article> articles = lastArticleId == null?
                articleRepository.findAllInfiniteScroll(boardId,pageSize):
                articleRepository.findAllInfiniteScroll(boardId, pageSize, lastArticleId);
        return articles.stream()
                .map(ArticleResponse :: form)
                .toList();
    }




    @Transactional
    public void delete(Long articleId)
    {
        Article article = articleRepository.findById(articleId).orElseThrow();
        boardArticleCountRepository.decrease(article.getBoardId());
        articleRepository.delete(article);
        outboxEventPublisher.publish(
                EventType.ARTICLE_DELETED,
                ArticleUpdatedEventPayload.builder()
                        .articleId(article.getArticleId())
                        .title(article.getTitle())
                        .content(article.getContent())
                        .boardId(article.getBoardId())
                        .writerId(article.getWriterId())
                        .createdAt(article.getCreatedAt())
                        .modifiedAt(article.getModifiedAt())
                        .build(),
                article.getBoardId()//shardKey
        );

    }


    public Long count(Long boardId)
    {
        return boardArticleCountRepository.findById(boardId)
                .map(BoardArticleCount::getArticleCount)
                .orElse(0L);
    }

}
