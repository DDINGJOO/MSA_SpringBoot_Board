package dding.board.common.outboxmessagerelay;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OutboxRepository  extends JpaRepository<Outbox, Long> {
    List<Outbox> findALlByShardKeyAndCreatedAtLessThanEqualOrderByCreatedAtAsc(
           Long shardKey,
           LocalDateTime time,
           Pageable pageable
    );
}
