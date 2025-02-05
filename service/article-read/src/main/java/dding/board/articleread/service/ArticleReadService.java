package dding.board.articleread.service;


import dding.board.articleread.client.ArticleClient;
import dding.board.articleread.client.CommentClient;
import dding.board.articleread.client.LikeClient;
import dding.board.articleread.client.ViewClient;
import dding.board.articleread.dto.response.ArticleReadResponse;
import dding.board.articleread.repository.ArticleQueryModel;
import dding.board.articleread.repository.ArticleQueryModelRepository;
import dding.board.articleread.service.event.handler.EventHandler;
import dding.board.common.event.Event;
import dding.board.common.event.EventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
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
