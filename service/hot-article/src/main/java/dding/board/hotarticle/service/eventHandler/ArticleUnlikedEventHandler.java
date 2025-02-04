package dding.board.hotarticle.service.eventHandler;

import dding.board.common.event.Event;
import dding.board.common.event.EventPayload;
import dding.board.common.event.EventType;
import dding.board.common.event.payload.ArticleLikedEventPayload;
import dding.board.common.event.payload.ArticleUnlikedEventPayload;
import dding.board.hotarticle.repository.ArticleLikeCountRepository;
import dding.board.hotarticle.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleUnlikedEventHandler implements  EventHandler<ArticleUnlikedEventPayload> {
    private final ArticleLikeCountRepository articleLikeCountRepository;
    @Override
    public void handle(Event<ArticleUnlikedEventPayload> event) {
        ArticleUnlikedEventPayload payload = event.getPayload();
        articleLikeCountRepository.createOrUpdate(
                payload.getArticleId(),
                payload.getArticleLikeCount(),
                TimeCalculatorUtils.calculateDurationToMidnight()
        );
    }

    @Override
    public boolean supports(Event<ArticleUnlikedEventPayload> event) {
        return EventType.ARTICLE_UNLIKED == event.getType();
    }

    @Override
    public Long findArticleId(Event<ArticleUnlikedEventPayload> evnet) {
        return evnet.getPayload().getArticleId();
    }
}
