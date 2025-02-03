package dding.board.common.event.payload;

import dding.board.common.event.EventPayload;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleLikedEventPayload implements EventPayload {

    private Long articleLikeId;
    private Long articleId;
    private Long userId;
    private LocalDateTime createdAt;
    private Long articleLikeCount;
}
