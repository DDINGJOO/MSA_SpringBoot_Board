package dding.board.articleread.service.event.handler;


import dding.board.articleread.repository.ArticleQueryModel;
import dding.board.articleread.repository.ArticleQueryModelRepository;
import dding.board.common.event.Event;
import dding.board.common.event.EventType;
import dding.board.common.event.payload.ArticleCreatedEventPayload;
import dding.board.common.event.payload.ArticleUpdatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ArticleUpdateddEventHandler implements EventHandler<ArticleUpdatedEventPayload> {
    private final ArticleQueryModelRepository articleQueryModelRepository;

    @Override
    public void handle(Event<ArticleUpdatedEventPayload> event) {
        ArticleUpdatedEventPayload  payload = event.getPayload();
       articleQueryModelRepository.read(event.getPayload().getArticleId())
               .ifPresent(articleQueryModel ->
                       articleQueryModel.updateBy(event.getPayload()));
    }

    @Override
    public boolean supports(Event<ArticleUpdatedEventPayload> event) {
        return EventType.ARTICLE_UPDATED == event.getType();
    }
}
