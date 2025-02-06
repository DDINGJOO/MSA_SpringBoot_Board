package dding.board.articleread.service.event.handler;


import dding.board.articleread.repository.ArticleIdListRepository;
import dding.board.articleread.repository.ArticleQueryModelRepository;
import dding.board.articleread.repository.BoardArticleCountRepository;
import dding.board.board.event.Event;
import dding.board.board.event.EventType;
import dding.board.board.event.payload.ArticleDeletedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {
    private final ArticleQueryModelRepository articleQueryModelRepository;
    private final ArticleIdListRepository articleIdListRepository;
    private final BoardArticleCountRepository boardArticleCountRepository;


    @Override
    public void handle(Event<ArticleDeletedEventPayload> event) {
        ArticleDeletedEventPayload  payload = event.getPayload();
        articleIdListRepository.delete(payload.getBoardId(), payload.getArticleId());
        boardArticleCountRepository.createOrUpdate(payload.getBoardId(),payload.getBoardArticleCount());
        articleQueryModelRepository.delete(payload.getArticleId());
    }

    @Override
    public boolean supports(Event<ArticleDeletedEventPayload> event) {
        return EventType.ARTICLE_DELETED == event.getType();
    }
}
