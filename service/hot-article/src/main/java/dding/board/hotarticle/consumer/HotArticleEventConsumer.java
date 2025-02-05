package dding.board.hotarticle.consumer;

import dding.board.common.event.Event;
import dding.board.common.event.EventPayload;
import dding.board.common.event.EventType;
import dding.board.hotarticle.service.HotArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotArticleEventConsumer {
    private final HotArticleService hotArticleService;

    @KafkaListener(topics = {
            EventType.Topic.DDING_BOARD_ARTICLE,
            EventType.Topic.DDING_BOARD_COMMENT,
            EventType.Topic.DDING_BOARD_LIKE,
            EventType.Topic.DDING_BOARD_VIEW
    })
    public void listen(String message, Acknowledgment ack) {
        log.info("[HotArticleEventConsumer.listen] received message={}", message);
        Event<EventPayload> event = Event.fromJson(message);
        if (event != null) {
            hotArticleService.handleEvent(event);
        }
        ack.acknowledge();
    }
}
