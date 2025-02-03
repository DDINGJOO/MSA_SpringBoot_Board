package dding.board.common.event;

import dding.board.common.event.payload.ArticleCreatedEventPayload;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class EventTest {

    @Test
    void serde(){
        ArticleCreatedEventPayload payload = ArticleCreatedEventPayload.builder()
                .articleId(1L)
                .title("title")
                .content("content")
                .boardId(1L)
                .writerId(1L)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .boardArticleCount(23L)
                .build();

        Event<EventPayload> event = Event.of(
                1234L,
                EventType.ARTICLE_CREATED,
                payload
        );
        String json = event.tojson();
        System.out.println("json = "+json);

        //when
        Event<EventPayload> result = Event.fromjson(json);

        //then
        Assertions.assertThat(result.getEventId()).isEqualTo(event.getEventId());
        Assertions.assertThat(result.getType()).isEqualTo(event.getType());
        Assertions.assertThat(result.getPayload()).isInstanceOf(payload.getClass());


        ArticleCreatedEventPayload resultPayload = (ArticleCreatedEventPayload)result.getPayload();

        Assertions.assertThat(resultPayload.getArticleId()).isEqualTo(payload.getArticleId());
        Assertions.assertThat(resultPayload.getCreatedAt()).isEqualTo(payload.getCreatedAt());

    }
}
