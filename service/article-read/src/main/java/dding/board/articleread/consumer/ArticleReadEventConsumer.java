package dding.board.articleread.consumer;


import dding.board.articleread.service.ArticleReadService;
import dding.board.common.event.Event;
import dding.board.common.event.EventPayload;
import dding.board.common.event.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleReadEventConsumer {

    private final ArticleReadService articleReadService;


    @KafkaListener(topics = {
            EventType.Topic.DDING_BOARD_ARTICLE,
            EventType.Topic.DDING_BOARD_COMMENT,
            EventType.Topic.DDING_BOARD_LIKE
    })
    public void listen(String message, Acknowledgment ack){
        log.info("[ArticleReadEventConsumer.listen] messgae ={}", message);

        Event<EventPayload> event = Event.fromJson(message);
        if(event!=null)
        {
            articleReadService.handleEvent(event);
        }
        ack.acknowledge();
    }
}
