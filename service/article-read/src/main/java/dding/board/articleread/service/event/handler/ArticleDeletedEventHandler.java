package dding.board.articleread.service.event.handler;


import dding.board.articleread.repository.ArticleQueryModelRepository;
import dding.board.common.event.Event;
import dding.board.common.event.EventType;
import dding.board.common.event.payload.ArticleDeletedEventPayload;
import dding.board.common.event.payload.ArticleUpdatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {
    private final ArticleQueryModelRepository articleQueryModelRepository;


    @Override
    public void handle(Event<ArticleDeletedEventPayload> event) {
        ArticleDeletedEventPayload  payload = event.getPayload();
        articleQueryModelRepository.delete(payload.getArticleId());

    }

    @Override
    public boolean supports(Event<ArticleDeletedEventPayload> event) {
        return EventType.ARTICLE_DELETED == event.getType();
    }
}
