package dding.board.board.outboxmessagerelay;

import dding.board.board.PrimaryKeyProvider.PrimaryIdProvider;
import dding.board.board.event.Event;
import dding.board.board.event.EventPayload;
import dding.board.board.event.EventType;
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
                ).toJson(),
                shardKey % MessageRelayConstants.SHARD_COUNT
        );
        applicationEventPublisher.publishEvent(OutboxEvent.of(outbox));
    }

}
