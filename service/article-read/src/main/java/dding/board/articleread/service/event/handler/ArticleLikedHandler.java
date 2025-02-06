package dding.board.articleread.service.event.handler;

import dding.board.articleread.repository.ArticleQueryModelRepository;
import dding.board.board.event.Event;
import dding.board.board.event.EventType;
import dding.board.board.event.payload.ArticleLikedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleLikedHandler implements EventHandler<ArticleLikedEventPayload>{
    private final ArticleQueryModelRepository articleQueryModelRepository;


    @Override
    public void handle(Event<ArticleLikedEventPayload> event) {
        articleQueryModelRepository.read(event.getPayload().getArticleId())
                .ifPresent(articleQueryModel ->
                        articleQueryModel.updateBy(event.getPayload()));
    }

    @Override
    public boolean supports(Event<ArticleLikedEventPayload> event) {
        return EventType.ARTICLE_LIKED == event.getType();
    }
}
