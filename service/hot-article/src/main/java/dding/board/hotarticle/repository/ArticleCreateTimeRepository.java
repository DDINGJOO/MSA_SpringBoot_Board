package dding.board.hotarticle.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Repository
@RequiredArgsConstructor
public class ArticleCreateTimeRepository {
    private final StringRedisTemplate redisTemplate;

    //hot-article::article::{articleId}::created-time
    private static final String KET_FORMAT = "hot-article::article::%s::created-time";

    public void createOrUpdate(Long articleId, LocalDateTime createdAt, Duration ttl) {
        redisTemplate.opsForValue().set(generateKey(articleId), String.valueOf(createdAt.toInstant(ZoneOffset.UTC).toEpochMilli()), ttl);
    }

    public void delete(Long articleId)
    {
        redisTemplate.delete(generateKey(articleId));
    }

    public LocalDateTime read(Long articleId)
    {
        var result = redisTemplate.opsForValue().get(generateKey(articleId));
        if(result == null)
        {
            return null;
        }
         return LocalDateTime.ofInstant(
                 Instant.ofEpochMilli(Long.valueOf(result)), ZoneOffset.UTC);
    }
    public String generateKey(Long articleId) {
        return KET_FORMAT.formatted(articleId);
    }
}

