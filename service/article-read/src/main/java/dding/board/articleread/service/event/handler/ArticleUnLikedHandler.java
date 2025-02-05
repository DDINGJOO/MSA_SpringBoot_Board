package dding.board.articleread.service.event.handler;

import dding.board.articleread.repository.ArticleQueryModelRepository;
import dding.board.common.event.Event;
import dding.board.common.event.EventType;
import dding.board.common.event.payload.ArticleLikedEventPayload;
import dding.board.common.event.payload.ArticleUnlikedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleUnLikedHandler implements EventHandler<ArticleUnlikedEventPayload>{
    private final ArticleQueryModelRepository articleQueryModelRepository;


    @Override
    public void handle(Event<ArticleUnlikedEventPayload> event) {
        articleQueryModelRepository.read(event.getPayload().getArticleId())
                .ifPresent(articleQueryModel ->
                        articleQueryModel.updateBy(event.getPayload()));
    }

    @Override
    public boolean supports(Event<ArticleUnlikedEventPayload> event) {
        return EventType.ARTICLE_UNLIKED == event.getType();
    }
}
