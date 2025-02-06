package dding.board.articleread.service.event.handler;

import dding.board.articleread.repository.ArticleQueryModelRepository;
import dding.board.board.event.Event;
import dding.board.board.event.EventType;
import dding.board.board.event.payload.CommentDeletedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentDeletedEventHandler implements EventHandler<CommentDeletedEventPayload>{
    private final ArticleQueryModelRepository articleQueryModelRepository;


    @Override
    public void handle(Event<CommentDeletedEventPayload> event) {
        articleQueryModelRepository.read(event.getPayload().getArticleId())
                .ifPresent(articleQueryModel ->
                        articleQueryModel.updateBy(event.getPayload()));
    }

    @Override
    public boolean supports(Event<CommentDeletedEventPayload> event) {
        return EventType.COMMENT_DELETED == event.getType();
    }

}
