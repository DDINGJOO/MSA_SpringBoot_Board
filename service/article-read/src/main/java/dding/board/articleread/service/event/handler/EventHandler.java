package dding.board.articleread.service.event.handler;

import dding.board.board.event.Event;
import dding.board.board.event.EventPayload;

public interface EventHandler <T extends EventPayload> {
    void handle(Event<T> event);
    boolean supports(Event<T> event);
}
