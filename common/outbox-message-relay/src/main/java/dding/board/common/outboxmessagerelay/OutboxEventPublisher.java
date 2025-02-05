package dding.board.common.outboxmessagerelay;

import dding.board.common.PrimaryKeyProvider.PrimaryIdProvider;
import dding.board.common.event.Event;
import dding.board.common.event.EventPayload;
import dding.board.common.event.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxEventPublisher {
    private final PrimaryIdProvider outboxIdProvider = new PrimaryIdProvider();
    private final PrimaryIdProvider eventIdProvider = new PrimaryIdProvider();
    private final ApplicationEventPublisher applicationEventPublisher;


    public void publish(EventType type, EventPayload payload, Long shardKey)
    {
        Outbox outbox = Outbox.create(
                outboxIdProvider.getId(),
                type,
                Event.of(
                        eventIdProvider.getId(),type,payload
                ).tojson(),
                shardKey % MessageRelayConstants.SHARD_COUNT
        );
        applicationEventPublisher.publishEvent(OutboxEvent.of(outbox));
    }

}
