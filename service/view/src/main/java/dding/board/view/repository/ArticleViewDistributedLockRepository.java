package dding.board.view.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class ArticleViewDistributedLockRepository {

    private final StringRedisTemplate redisTemplate;


    //view :: article::{articleId}::user::{userId}::lock
    private static final String KEY_FORMAT = "article::@s::user::%s::lock";

    public boolean lock(Long articleId, Long userId, Duration ttl)
    {
        String key = generatedKey(articleId,userId);
        return redisTemplate.opsForValue().setIfAbsent(key,"",ttl);
    }


    private String generatedKey(Long articleId, Long userId)
    {
        return KEY_FORMAT.formatted(articleId,userId);
    }

}
