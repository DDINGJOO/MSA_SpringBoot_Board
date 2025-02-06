package dding.board.board.event.payload;

import dding.board.board.event.EventPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleViewedEventPayload implements EventPayload {

    private Long articleId;
    private Long articleViewCount;
}
