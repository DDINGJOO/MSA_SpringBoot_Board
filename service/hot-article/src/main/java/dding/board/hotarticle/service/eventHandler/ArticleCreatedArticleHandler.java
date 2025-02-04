package dding.board.hotarticle.service.eventHandler;

import dding.board.common.event.Event;
import dding.board.common.event.payload.ArticleCreatedEventPayload;
import dding.board.hotarticle.repository.ArticleCreateTimeRepository;
import dding.board.hotarticle.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleCreatedArticleHandler implements EventHandler<ArticleCreatedEventPayload> {
    private final ArticleCreateTimeRepository articleCreateTimeRepository;

    @Override
    public void handle(Event<ArticleCreatedEventPayload> event) {
        ArticleCreatedEventPayload payload = event.getPayload();
        articleCreateTimeRepository.createOrUpdate(
                payload.getArticleId(),
                payload.getCreatedAt(),
                TimeCalculatorUtils.calculateDurationToMidnight()
        );

    }

    @Override
    public boolean supports(Event<ArticleCreatedEventPayload> event) {
        return false;
    }

    @Override
    public Long findArticleId(Event<ArticleCreatedEventPayload> evnet) {
        return 0L;
    }
}
