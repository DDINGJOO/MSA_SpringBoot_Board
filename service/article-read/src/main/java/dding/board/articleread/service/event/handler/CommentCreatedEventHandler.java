package dding.board.articleread.service.event.handler;

import dding.board.articleread.repository.ArticleQueryModel;
import dding.board.articleread.repository.ArticleQueryModelRepository;
import dding.board.common.event.Event;
import dding.board.common.event.EventType;
import dding.board.common.event.payload.ArticleCreatedEventPayload;
import dding.board.common.event.payload.ArticleUpdatedEventPayload;
import dding.board.common.event.payload.CommentCreatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class CommentCreatedEventHandler  implements EventHandler<CommentCreatedEventPayload>{
    private final ArticleQueryModelRepository articleQueryModelRepository;


    @Override
    public void handle(Event<CommentCreatedEventPayload> event) {
        articleQueryModelRepository.read(event.getPayload().getArticleId())
                .ifPresent(articleQueryModel ->
                        articleQueryModel.updateBy(event.getPayload()));
    }

    @Override
    public boolean supports(Event<CommentCreatedEventPayload> event) {
        return EventType.COMMENT_CREATED == event.getType();
    }

}
