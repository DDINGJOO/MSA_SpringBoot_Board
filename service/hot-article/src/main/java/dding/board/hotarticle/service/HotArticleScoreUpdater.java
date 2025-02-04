package dding.board.hotarticle.service;


import dding.board.common.event.Event;
import dding.board.common.event.EventPayload;
import dding.board.hotarticle.repository.ArticleCreateTimeRepository;
import dding.board.hotarticle.repository.HotArticleListRepository;
import dding.board.hotarticle.service.eventHandler.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class HotArticleScoreUpdater {
    private final HotArticleListRepository hotArticleListRepository;
    private final HotArticleScoreCalculator hotArticleScoreCalculator;
    private final ArticleCreateTimeRepository articleCreateTimeRepository;


    private static final long HOT_ARTICLE_COUNT = 10;
    private static final Duration HOT_ARTICLE_TTL = Duration.ofDays(10);


    public void update(Event<EventPayload> event, EventHandler<EventPayload> eventHandler) {
        Long articleId = eventHandler.findArticleId(event);
        var createdTime = articleCreateTimeRepository.read(articleId);
        if(!isArticleCreatedToday(createdTime))
        {
            return;
        }

        eventHandler.handle(event);

        Long score = hotArticleScoreCalculator.calculate(articleId);
        hotArticleListRepository.add(
                articleId,
                createdTime,
                score,
                HOT_ARTICLE_COUNT,
                HOT_ARTICLE_TTL);
    }

    private boolean isArticleCreatedToday(LocalDateTime createdTime) {
        return createdTime != null && createdTime.toLocalDate().equals(LocalDate.now());
    }
}
