package dding.board.articleread.service.event.handler;

import dding.board.articleread.repository.ArticleQueryModelRepository;
import dding.board.board.event.Event;
import dding.board.board.event.EventType;
import dding.board.board.event.payload.CommentCreatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
