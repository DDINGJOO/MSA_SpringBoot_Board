package dding.board.articleread.service;


import dding.board.articleread.client.ArticleClient;
import dding.board.articleread.client.CommentClient;
import dding.board.articleread.client.LikeClient;
import dding.board.articleread.client.ViewClient;
import dding.board.articleread.dto.response.ArticleReadPageResponse;
import dding.board.articleread.dto.response.ArticleReadResponse;
import dding.board.articleread.repository.ArticleIdListRepository;
import dding.board.articleread.repository.ArticleQueryModel;
import dding.board.articleread.repository.ArticleQueryModelRepository;
import dding.board.articleread.repository.BoardArticleCountRepository;
import dding.board.articleread.service.event.handler.EventHandler;
import dding.board.common.event.Event;
import dding.board.common.event.EventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleReadService {
    private final ArticleClient articleClient;
    private final CommentClient commentClient;
    private final LikeClient likeClient;
    private final ViewClient viewClient;
    private final ArticleQueryModelRepository articleQueryModelRepository;
    private final ArticleIdListRepository articleIdListRepository;
    private final BoardArticleCountRepository boardArticleCountRepository;
    private final List<EventHandler> eventHandlers;


    public void handleEvent(Event<EventPayload> event)
    {
        for(EventHandler eventHandler :eventHandlers)
        {
            if(eventHandler.supports(event))
            {
                eventHandler.handle(event);
            }
        }
    }

    public ArticleReadResponse read(Long articleId)
    {
        ArticleQueryModel articleQueryModel = articleQueryModelRepository.read(articleId)
                .or(() -> fetch(articleId))
                .orElseThrow();
        return ArticleReadResponse.from(
                articleQueryModel,
                viewClient.count(articleId)
        );
    }

    public ArticleReadPageResponse readAll(Long boardId, Long page, Long pageSize)
    {
        return ArticleReadPageResponse.of(
                readAll(
                        readAllArticleIds(boardId, page,pageSize)
                ),
                count(boardId)
        );
    }






    private List<Long> readAllInfiniteScrollIds(Long boardId, Long lastArticleId, Long pageSize) {

        List<Long> articleIds = articleIdListRepository.readAllInfiniteScroll(boardId,lastArticleId,pageSize);
        if(pageSize == articleIds.size()){
            log.info("[ArticleReadService.readAllInfiniteScrollIdsArticleIds] return from redis database");
            return articleIds;
        }

        log.info("[ArticleReadService.readAllInfiniteScrollIdsArticleIds] return from origin database");
        return articleClient.readAllInfiniteScroll(boardId,lastArticleId,pageSize).stream()
                .map(ArticleClient.ArticleResponse::getArticleId)
                .toList();

    }



    public List<ArticleReadResponse> readAll(List<Long> articleIds)
    {
        Map<Long, ArticleQueryModel> articleQueryModelMap =  articleQueryModelRepository.readAll(articleIds);
        return articleIds.stream()
                .map(articleId -> articleQueryModelMap.containsKey(articleId) ?
                        articleQueryModelMap.get(articleId) :
                        fetch(articleId).orElse(null))
                .filter(Objects::nonNull)
                .map(articleQueryModel ->
                        ArticleReadResponse.from(
                                articleQueryModel,
                                viewClient.count(articleQueryModel.getArticleId())
                        )).toList();
    }







    private List<Long> readAllArticleIds(Long boardId, Long page, Long pageSize)
    {
        List<Long> articleIds = articleIdListRepository.readAll(boardId, (page-1)*pageSize, pageSize);
        if(pageSize == articleIds.size()){
            log.info("[ArticleReadService.readAllArticleIds] return from redis database");
            return articleIds;
        }

        log.info("[ArticleReadService.readAllArticleIds] return from origin database");
        return articleClient.readAll(boardId,page,pageSize).getArticles().stream()
                .map(ArticleClient.ArticleResponse::getArticleId)
                .toList();
    }

    private Long count(Long boardId)
    {
        Long result = boardArticleCountRepository.read(boardId);
        if(result != null)
        {
            return result;
        }

        long count = articleClient.count(boardId);
        boardArticleCountRepository.createOrUpdate(boardId,count);
        return count;
    }

    public List<ArticleReadResponse> readAllInfiniteScroll(Long boardId, Long lastArticleId, Long pageSize)
    {
        return readAll(
                readAllInfiniteScrollIds(boardId, lastArticleId,pageSize)
        );
    }




    private  Optional<ArticleQueryModel> fetch(Long articleId) {
        Optional<ArticleQueryModel> articleQueryModelOptional = articleClient.read(articleId)
                .map(article -> ArticleQueryModel.create(
                        article,
                        commentClient.count(articleId),
                        likeClient.count(articleId)
                ));
        articleQueryModelOptional
                .ifPresent(articleQueryModel -> articleQueryModelRepository.create(
                        articleQueryModel, Duration.ofDays(1)
                ));

        log.info("[ArticleReadService.fetch] fetch date. articleId = {}, isPresent ={}", articleClient, articleQueryModelOptional.isPresent());
        return articleQueryModelOptional;
    }
}
