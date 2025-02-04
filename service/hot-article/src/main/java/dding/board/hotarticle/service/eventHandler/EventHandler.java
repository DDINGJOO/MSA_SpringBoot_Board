package dding.board.hotarticle.service.eventHandler;

import dding.board.common.event.Event;
import dding.board.common.event.EventPayload;

public interface EventHandler <T extends EventPayload> {
    void handle(Event<T> event);
    boolean supports(Event<T> event);
    Long findArticleId(Event<T> evnet);
}
