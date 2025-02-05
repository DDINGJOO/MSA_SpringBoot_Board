package dding.board.articleread.service.event.handler;


import dding.board.articleread.repository.ArticleQueryModel;
import dding.board.articleread.repository.ArticleQueryModelRepository;
import dding.board.common.event.Event;
import dding.board.common.event.EventType;
import dding.board.common.event.payload.ArticleCreatedEventPayload;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ArticleCreatedEventHandler implements EventHandler<ArticleCreatedEventPayload> {
    private final ArticleQueryModelRepository articleQueryModelRepository;

    @Override
    public void handle(Event<ArticleCreatedEventPayload> event) {
        ArticleCreatedEventPayload  payload = event.getPayload();
        articleQueryModelRepository.create(
                ArticleQueryModel.create(payload),
                Duration.ofDays(1)
        );
    }

    @Override
    public boolean supports(Event<ArticleCreatedEventPayload> event) {
        return EventType.ARTICLE_CREATED == event.getType();
    }
}
